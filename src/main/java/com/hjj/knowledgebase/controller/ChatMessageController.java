package com.hjj.knowledgebase.controller;

import com.hjj.knowledgebase.common.BaseResponse;
import com.hjj.knowledgebase.common.ResultUtils;
import com.hjj.knowledgebase.model.dto.chatconversation.ChatConversationDto;
import com.hjj.knowledgebase.model.entity.ChatMessage;
import com.hjj.knowledgebase.model.vo.ChatMessageVo;
import com.hjj.knowledgebase.service.ChatConversationService;
import com.hjj.knowledgebase.service.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/chat")
@Tag(name = "聊天消息")
public class ChatMessageController {

    @Resource
    private ChatClient chatClient;

    @Resource
    private ChatConversationService chatConversationService;

    @Resource
    private ChatMessageService chatMessageService;

    @GetMapping(value = "/completion", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(description = "AI对话", summary = "AI对话")
    public Flux<String> completion(@RequestParam("question") String message,
                                   @RequestParam("conversationId") Long conversationId,
                                   HttpServletRequest request) {
        if (conversationId == null || conversationId == 0) {
            ChatConversationDto chatConversationDto = new ChatConversationDto();
            chatConversationDto.setName(message);
            conversationId = chatConversationService.add(chatConversationDto);
        }
        Flux<String> content = chatClient.prompt().user(message).stream().content();
        return content;
    }

    @GetMapping(value = "/rag", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(description = "知识库检索", summary = "知识库检索")
    public Flux<ChatMessageVo> rag(@RequestParam("question") String question,
                                   @RequestParam("conversationId") Long conversationId,
                                   HttpServletRequest request) {
        return chatMessageService.rag(question, conversationId, request);
    }

    @GetMapping("/list/{id}")
    public BaseResponse<List<ChatMessage>> list(@PathVariable("id") Long conversationId) {
        List<ChatMessage> chatMessages = chatMessageService.lambdaQuery()
                .eq(conversationId != null && conversationId != 0, ChatMessage::getConversationId, conversationId)
                .list();
        return ResultUtils.success(chatMessages);
    }

}
