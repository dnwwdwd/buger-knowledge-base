package com.hjj.knowledgebase.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjj.knowledgebase.common.ErrorCode;
import com.hjj.knowledgebase.exception.BusinessException;
import com.hjj.knowledgebase.model.dto.chatconversation.ChatConversationDto;
import com.hjj.knowledgebase.model.entity.AiDocument;
import com.hjj.knowledgebase.model.entity.ChatMessage;
import com.hjj.knowledgebase.model.vo.ChatMessageVo;
import com.hjj.knowledgebase.rag.RagService;
import com.hjj.knowledgebase.rag.utils.MyTikaDocumentReader;
import com.hjj.knowledgebase.rag.utils.MyTokenTextSplitter;
import com.hjj.knowledgebase.service.AiDocumentService;
import com.hjj.knowledgebase.service.ChatConversationService;
import com.hjj.knowledgebase.service.ChatMessageService;
import com.hjj.knowledgebase.mapper.ChatMessageMapper;
import com.hjj.knowledgebase.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.ai.document.Document;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author hejiajun
 * @description 针对表【chat_message(聊天消息)】的数据库操作Service实现
 * @createDate 2025-07-05 16:06:47
 */
@Service
@Slf4j
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage>
        implements ChatMessageService {

    @Resource
    private MyTokenTextSplitter myTokenTextSplitter;

    @Resource
    private MyTikaDocumentReader myTikaDocumentReader;

    @Resource
    private RagService ragService;

    @Resource
    private AiDocumentService aiDocumentService;

    @Resource
    private UserService userService;

    @Resource
    @Lazy
    private ChatConversationService chatConversationService;


    @Override
    public List<ChatMessage> list(Long conversationId) {
        return this.lambdaQuery()
                .eq(ChatMessage::getConversationId, conversationId)
                .list();
    }

    @Override
    public synchronized Boolean upload(MultipartFile file, HttpServletRequest request) {
        String suffix = FileUtil.getSuffix(file.getOriginalFilename());
        List<String> suffixes = List.of("pdf", "ppt", "pptx", "doc", "docx");
        if (!suffixes.contains(suffix)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "仅支持上传pdf、ppt、pptx、doc、docx文件");
        }
        Long userId = userService.getLoginUser(request).getId();
        String dir = System.getProperty("java.io.tmpdir");
        String filePath = dir + file.getOriginalFilename();
        String filename = FileNameUtil.mainName(file.getOriginalFilename());
        LambdaQueryWrapper<AiDocument> wrapper = new LambdaQueryWrapper<AiDocument>()
                .eq(AiDocument::getName, filename)
                .eq(AiDocument::getCreateBy, userId);
        AiDocument aiDocument = aiDocumentService.getOne(wrapper);
        if (aiDocument != null) {
            String documentRagIdsStr = aiDocument.getDocumentRagIds();
            List<String> documentRagIds = JSONUtil.toList(documentRagIdsStr, String.class);
            ragService.delDocumentsFromEsRag(documentRagIds);
            aiDocumentService.remove(wrapper);
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
            aiDocumentService.save(aiDocument);
            return CollectionUtils.isNotEmpty(documentIds);
        } catch (Exception e) {
            log.error("文件：{} 写入本地失败", filename);
            return false;
        } finally {
            FileUtil.del(filePath);
        }
    }

    @Override
    public Flux<ChatMessageVo> rag(String question, Long conversationId, HttpServletRequest request) {
        if (conversationId == null || conversationId == 0) {
            conversationId = chatConversationService.add(ChatConversationDto
                    .builder().name(question).build());
        }
        return ragService.queryFromEsRag(question, conversationId);
    }
}




