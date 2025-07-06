package com.hjj.knowledgebase.model.vo;

import com.hjj.knowledgebase.model.entity.ChatMessage;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChatConversationVo implements Serializable {

    private Long id;

    private String name;

    private Long createBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private List<ChatMessage> chatMessages;

}
