package com.hjj.knowledgebase.controller;

import com.hjj.knowledgebase.common.BaseResponse;
import com.hjj.knowledgebase.common.ResultUtils;
import com.hjj.knowledgebase.model.dto.chatconversation.ChatConversationDto;
import com.hjj.knowledgebase.model.vo.ChatMessageVo;
import com.hjj.knowledgebase.service.ChatConversationService;
import com.hjj.knowledgebase.service.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

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
            conversationId = chatConversationService.add(new ChatConversationDto(message), request);
        }
        Flux<String> content = chatClient.prompt().user(message).stream().content();
        return content;
    }

    @PostMapping("/upload")
    @Operation(description = "文件上传", summary = "文件上传")
    public BaseResponse<Boolean> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        return ResultUtils.success(chatMessageService.upload(file, request));
    }

    @GetMapping(value = "/rag", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(description = "知识库检索", summary = "知识库检索")
    public Flux<ChatMessageVo> rag(@RequestParam("question") String question,
                                   @RequestParam("conversationId") Long conversationId,
                                   HttpServletRequest request) {
        return chatMessageService.rag(question, conversationId, request);
    }

}
