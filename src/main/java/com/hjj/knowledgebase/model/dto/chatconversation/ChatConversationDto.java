package com.hjj.knowledgebase.model.dto.chatconversation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatConversationDto implements Serializable {

    private String name;

}
