package com.hjj.knowledgebase.rag.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.elasticsearch.ElasticsearchVectorStore;
import org.springframework.ai.vectorstore.elasticsearch.ElasticsearchVectorStoreOptions;
import org.springframework.ai.vectorstore.elasticsearch.SimilarityFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsRagConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder(new HttpHost("localhost", 9200, "http"))
                .build();
    }

    @Bean
    public VectorStore vectorStore(RestClient restClient, EmbeddingModel embeddingModel) {
        ElasticsearchVectorStoreOptions options = new ElasticsearchVectorStoreOptions();
        options.setIndexName("custom-index");
        options.setSimilarity(SimilarityFunction.cosine);
        options.setDimensions(1024);

        return ElasticsearchVectorStore.builder(restClient, embeddingModel)
                .options(options)
                .initializeSchema(true)
                .batchingStrategy(new TokenCountBatchingStrategy())
                .build();
    }


}
