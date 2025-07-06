package com.hjj.knowledgebase.config;

import com.hjj.knowledgebase.advisor.LoggerAdvisor;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OllamaConfig {

    @Resource
    private ChatModel ollamaChatModel;

    @Resource
    private LoggerAdvisor loggerAdvisor;

    @Bean
    public ChatClient chatClient() {
        return ChatClient.builder(ollamaChatModel).defaultAdvisors(loggerAdvisor).build();
    }

}
