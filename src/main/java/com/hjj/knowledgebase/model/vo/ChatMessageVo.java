package com.hjj.knowledgebase.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageVo implements Serializable {

    private Long conversationId;

    private String content;

    private String role;

}
