package com.hjj.knowledgebase.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.model.MessageAggregator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class LoggerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private AdvisedRequest before(AdvisedRequest request) {
        log.info("AI Request: {}", request.userText());
        return request;
    }

    private void observeAfter(AdvisedResponse advisedResponse) {
        log.info("AI Response: {}", advisedResponse.response().getResult().getOutput().getText());
    }

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        advisedRequest = before(advisedRequest);
        AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);
        observeAfter(advisedResponse);
        return advisedResponse;
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        advisedRequest = before(advisedRequest);
        Flux<AdvisedResponse> advisedResponses = chain.nextAroundStream(advisedRequest);
        return new MessageAggregator().aggregateAdvisedResponse(advisedResponses, this::observeAfter);
    }

}
