package com.hjj.knowledgebase.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjj.knowledgebase.mapper.ChatConversationMapper;
import com.hjj.knowledgebase.model.dto.chatconversation.ChatConversationDto;
import com.hjj.knowledgebase.model.entity.ChatConversation;
import com.hjj.knowledgebase.model.entity.ChatMessage;
import com.hjj.knowledgebase.model.vo.ChatConversationVo;
import com.hjj.knowledgebase.service.ChatConversationService;
import com.hjj.knowledgebase.service.ChatMessageService;
import com.hjj.knowledgebase.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hejiajun
 * @description 针对表【chat_conversation(聊天对话)】的数据库操作Service实现
 * @createDate 2025-07-05 16:06:47
 */
@Service
public class ChatConversationServiceImpl extends ServiceImpl<ChatConversationMapper, ChatConversation>
        implements ChatConversationService {

    @Resource
    private UserService userService;

    @Resource
    private ChatMessageService chatMessageService;

    @Override
    public Long add(ChatConversationDto dto) {
        ChatConversation chatConversation = new ChatConversation();
        chatConversation.setCreateBy(StpUtil.getLoginIdAsLong());
        chatConversation.setName(dto.getName());
        this.save(chatConversation);
        return chatConversation.getId();
    }

    @Override
    public Boolean deleteByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return true;
        }
        boolean b1 = this.removeByIds(ids);
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ChatMessage::getConversationId, ids);
        return chatMessageService.remove(wrapper) && b1;
    }

    public List<ChatConversationVo> list(HttpServletRequest request) {
        Long userId = userService.getLoginUser(request).getId();
        List<ChatConversation> chatConversations = this.lambdaQuery()
                .eq(ChatConversation::getCreateBy, userId)
                .list();
        return chatConversations.stream().map(chatConversation -> {
            ChatConversationVo chatConversationVo = CglibUtil.copy(chatConversation, ChatConversationVo.class);
            List<ChatMessage> chatMessages = chatMessageService.list(chatConversation.getId());
            chatConversationVo.setChatMessages(chatMessages);
            return chatConversationVo;
        }).toList();

    }

}




