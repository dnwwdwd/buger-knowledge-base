package com.hjj.knowledgebase.enums;

import lombok.Getter;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;

@Getter
public enum MessageRoleEnum {

    USER("user", UserMessage.class),
    SYSTEM("system", SystemMessage.class),
    AISSISTANT("assistant", AssistantMessage.class);

    private String name;
    private Class<? extends org.springframework.ai.chat.messages.Message> clazz;

    MessageRoleEnum(String name, Class<? extends org.springframework.ai.chat.messages.Message> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public static MessageRoleEnum getByValue(String value) {
        for (MessageRoleEnum messageRoleEnum : MessageRoleEnum.values()) {
            if (messageRoleEnum.name.equals(value)) {
                return messageRoleEnum;
            }
        }
        return null;
    }

}
