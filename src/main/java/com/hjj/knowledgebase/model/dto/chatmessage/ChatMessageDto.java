package com.hjj.knowledgebase.model.dto.chatmessage;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChatMessageDto implements Serializable {

    private Long conversationId;

    private String question;

}
