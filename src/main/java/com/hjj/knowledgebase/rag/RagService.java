package com.hjj.knowledgebase.rag;

import com.hjj.knowledgebase.chatmemory.MySQLChatMemory;
import com.hjj.knowledgebase.model.vo.ChatMessageVo;
import com.hjj.knowledgebase.service.AiDocumentService;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.elasticsearch.ElasticsearchVectorStore;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class RagService {

    @Resource
    private AiDocumentService aiDocumentService;

    @Resource
    private ElasticsearchVectorStore vectorStore;

    @Resource
    private ChatClient chatClient;

    @Resource
    private MySQLChatMemory mySQLChatMemory;

    public Flux<ChatMessageVo> queryFromEsRag(String question, Long conversationId) {

        Advisor advisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .similarityThreshold(0.5)
                        .topK(3)
                        .vectorStore(vectorStore)
                        .build())
                .build();

        return chatClient.prompt()
                .user(question)
                .advisors(advisor, new MessageChatMemoryAdvisor(mySQLChatMemory))
                .advisors(advisorSpec -> advisorSpec.param("conversationId", conversationId))
                .stream()
                .content()
                .map(response -> ChatMessageVo.builder().
                        content(response)
                        .conversationId(conversationId)
                        .build());

    }

    public List<String> saveDocumentToEsRag(List<Document> documents) {
        vectorStore.add(documents);
        return documents.stream().map(Document::getId).toList();
    }


    public void delDocumentsFromEsRag(List<String> documentRagIds) {
        vectorStore.delete(documentRagIds);
    }
}
