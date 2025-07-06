package com.hjj.knowledgebase.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjj.knowledgebase.model.entity.ChatMessage;
import com.hjj.knowledgebase.model.vo.ChatMessageVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author hejiajun
 * @description 针对表【chat_message(聊天消息)】的数据库操作Service
 * @createDate 2025-07-05 16:06:47
 */
public interface ChatMessageService extends IService<ChatMessage> {

    List<ChatMessage> list(Long conversationId);

    Boolean upload(MultipartFile file, HttpServletRequest request);

    Flux<ChatMessageVo> rag(String question, Long conversationId, HttpServletRequest request);

}
