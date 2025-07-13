package com.hjj.knowledgebase.model.dto.chatconversation;

import com.hjj.knowledgebase.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

@Data
public class ChatConversationRequest extends PageRequest implements Serializable {

    private String name;

}
