package com.hjj.knowledgebase.service;

import com.hjj.knowledgebase.model.dto.chatconversation.ChatConversationDto;
import com.hjj.knowledgebase.model.entity.ChatConversation;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author hejiajun
* @description 针对表【chat_conversation(聊天对话)】的数据库操作Service
* @createDate 2025-07-05 16:06:47
*/
public interface ChatConversationService extends IService<ChatConversation> {

    Long add(ChatConversationDto dto);

    Boolean deleteByIds(List<Long> ids);
}
