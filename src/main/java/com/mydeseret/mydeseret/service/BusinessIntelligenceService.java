package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.config.TenantContext;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BusinessIntelligenceService {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    @Autowired
    public BusinessIntelligenceService(VectorStore vectorStore, ChatClient.Builder chatClientBuilder) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder.build();
    }

    public void ingestData(String content) {
        String tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            throw new IllegalStateException("No tenant context found for data ingestion");
        }

        Document document = new Document(content, Map.of("tenant", tenantId));
        vectorStore.add(List.of(document));
    }

    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = "aiService", fallbackMethod = "fallbackAskBusinessQuestion")
    public String askBusinessQuestion(String question) {
        String tenantId = com.mydeseret.mydeseret.config.TenantContext.getCurrentTenant();

        SearchRequest searchRequest = SearchRequest.builder()
                .query(question)
                .topK(3)
                .filterExpression("tenant == '" + tenantId + "'")
                .build();

        List<Document> similarDocs = vectorStore.similaritySearch(searchRequest);

        String context = similarDocs.stream()
                .map(doc -> doc.getText())
                .collect(Collectors.joining("\n"));

        String promptText = """
                You are an AI business analyst for Deseret Technologies.
                Use the following context to answer the user's question.
                If the answer is not in the context, say "I don't have enough data to answer that." or "The data is insufficient to provide an answer." or something sarcastic like that.

                Context:
                %s

                Question:
                %s
                """.formatted(context, question);

        return chatClient.prompt(promptText).call().content();
    }

    public String fallbackAskBusinessQuestion(String question, Throwable t) {
        return "The AI Analyst is currently unavailable. Please try again later. (Error: " + t.getMessage() + ")";
    }
}