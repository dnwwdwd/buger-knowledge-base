package com.hjj.knowledgebase.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjj.knowledgebase.common.ErrorCode;
import com.hjj.knowledgebase.exception.BusinessException;
import com.hjj.knowledgebase.model.entity.AiDocument;
import com.hjj.knowledgebase.rag.RagService;
import com.hjj.knowledgebase.rag.utils.MyTikaDocumentReader;
import com.hjj.knowledgebase.service.AiDocumentService;
import com.hjj.knowledgebase.mapper.AiDocumentMapper;
import com.hjj.knowledgebase.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.ai.document.Document;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author hejiajun
 * @description 针对表【ai_document(AI知识库文档)】的数据库操作Service实现
 * @createDate 2025-07-05 16:06:47
 */
@Service
@Slf4j
public class AiDocumentServiceImpl extends ServiceImpl<AiDocumentMapper, AiDocument>
        implements AiDocumentService {

    @Resource
    private UserService userService;

    @Resource
    @Lazy
    private RagService ragService;

    @Resource
    private MyTikaDocumentReader myTikaDocumentReader;

    @Override
    public Boolean upload(MultipartFile file, HttpServletRequest request) {
        String fileFullName = file.getOriginalFilename();
        String suffix = FileUtil.getSuffix(fileFullName);
        List<String> suffixes = List.of("pdf", "ppt", "pptx", "doc", "docx");
        if (!suffixes.contains(suffix)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "仅支持上传pdf、ppt、pptx、doc、docx文件");
        }
        Long userId = userService.getLoginUser(request).getId();
        String dir = System.getProperty("java.io.tmpdir");
        String filePath = dir + fileFullName;
        String filename = FileNameUtil.mainName(fileFullName);
        LambdaQueryWrapper<AiDocument> wrapper = new LambdaQueryWrapper<AiDocument>()
                .eq(AiDocument::getName, filename)
                .eq(AiDocument::getCreateBy, userId);
        AiDocument aiDocument = this.getOne(wrapper);
        if (aiDocument != null) {
            String documentRagIdsStr = aiDocument.getDocumentRagIds();
            List<String> documentRagIds = JSONUtil.toList(documentRagIdsStr, String.class);
            ragService.delDocumentsFromEsRag(documentRagIds);
            this.remove(wrapper);
        }
        try {
            FileUtil.writeFromStream(file.getInputStream(), filePath);
            FileSystemResource resource = new FileSystemResource(filePath);
            List<Document> documents = myTikaDocumentReader.loadAndSplitDocuments(resource);
            if (CollectionUtils.isEmpty(documents)) {
                return false;
            }
            List<String> documentIds = ragService.saveDocumentToEsRag(documents);
            aiDocument = new AiDocument();
            aiDocument.setName(filename);
            aiDocument.setSuffix(suffix);
            aiDocument.setSize(file.getSize() / 1024 * 1024D);
            aiDocument.setDocumentRagIds(JSONUtil.toJsonStr(documentIds));
            aiDocument.setCreateBy(userId);
            this.save(aiDocument);
            return CollectionUtils.isNotEmpty(documentIds);
        } catch (Exception e) {
            log.error("文件：{} 写入本地失败", filename);
            return false;
        } finally {
            FileUtil.del(filePath);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        AiDocument aiDocument = this.getById(id);
        if (aiDocument != null) {
            String documentRagIdsStr = aiDocument.getDocumentRagIds();
            List<String> documentRagIds = JSONUtil.toList(documentRagIdsStr, String.class);
            ragService.delDocumentsFromEsRag(documentRagIds);
            this.removeById(id);
        }
        return true;
    }
}




