package com.hjj.knowledgebase.controller;

import com.hjj.knowledgebase.common.BaseResponse;
import com.hjj.knowledgebase.common.ResultUtils;
import com.hjj.knowledgebase.model.dto.chatconversation.ChatConversationDto;
import com.hjj.knowledgebase.service.ChatConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat/conversation")
@Tag(name = "聊天对话")
public class ChatConversationController {

    @Resource
    private ChatConversationService chatConversationService;

    @PostMapping("/add")
    @Operation(description = "添加聊天对话", summary = "添加聊天对话")
    public BaseResponse<Long> add(@RequestBody ChatConversationDto dto, HttpServletRequest request) {
        Long conversationId = chatConversationService.add(dto, request);
        return ResultUtils.success(conversationId);
    }

}
