package com.hjj.knowledgebase.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjj.knowledgebase.common.BaseResponse;
import com.hjj.knowledgebase.common.ErrorCode;
import com.hjj.knowledgebase.common.ResultUtils;
import com.hjj.knowledgebase.exception.BusinessException;
import com.hjj.knowledgebase.model.dto.chatconversation.ChatConversationDto;
import com.hjj.knowledgebase.model.dto.chatconversation.ChatConversationRequest;
import com.hjj.knowledgebase.model.entity.ChatConversation;
import com.hjj.knowledgebase.service.ChatConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat/conversation")
@Tag(name = "聊天对话")
public class ChatConversationController {

    @Resource
    private ChatConversationService chatConversationService;

    @PostMapping("/add")
    @Operation(description = "添加聊天对话", summary = "添加聊天对话")
    public BaseResponse<Long> add(@RequestBody ChatConversationDto dto) {
        Long conversationId = chatConversationService.add(dto);
        return ResultUtils.success(conversationId);
    }

    @PostMapping("/page")
    @Operation(description = "分页", summary = "分页")
    public BaseResponse<Page<ChatConversation>> page(@RequestBody ChatConversationRequest request) {
        String name = request.getName();
        int current = request.getCurrent();
        int pageSize = request.getPageSize();
        Object userId = StpUtil.getLoginId();
        Page<ChatConversation> page = chatConversationService.lambdaQuery()
                .like(StringUtils.isNotBlank(name), ChatConversation::getName, name)
                .orderByDesc(ChatConversation::getCreateTime)
                .eq(ChatConversation::getCreateBy, userId)
                .page(new Page<>(current, pageSize));
        return ResultUtils.success(page);
    }

    @GetMapping("/{id}")
    public BaseResponse<ChatConversation> get(@PathVariable("id") Long id) {
        return ResultUtils.success(chatConversationService.getById(id));
    }


    @PostMapping("/delete/{ids}")
    public BaseResponse<Boolean> delete(@PathVariable("ids") List<Long> ids) {
        return ResultUtils.success(chatConversationService.deleteByIds(ids));
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> update(@RequestBody ChatConversationDto dto) {
        if (dto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ChatConversation chatConversation = BeanUtil.copyProperties(dto, ChatConversation.class);
        return ResultUtils.success(chatConversationService.updateById(chatConversation));
    }

}
