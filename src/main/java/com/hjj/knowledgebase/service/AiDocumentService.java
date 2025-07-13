package com.hjj.knowledgebase.service;

import com.hjj.knowledgebase.model.entity.AiDocument;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

/**
* @author hejiajun
* @description 针对表【ai_document(AI知识库文档)】的数据库操作Service
* @createDate 2025-07-05 16:06:47
*/
public interface AiDocumentService extends IService<AiDocument> {

    Boolean upload(MultipartFile file, HttpServletRequest request);

    Boolean delete(Long id);
}
