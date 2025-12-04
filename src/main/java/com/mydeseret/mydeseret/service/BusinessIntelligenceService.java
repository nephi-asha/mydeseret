package com.mydeseret.mydeseret.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        Document document = new Document(content);
        vectorStore.add(List.of(document));
    }

    public String askBusinessQuestion(String userQuestion) {
        SearchRequest request = SearchRequest.builder()
                .query(userQuestion)
                .topK(4)
                .build();

        List<Document> similarDocuments = vectorStore.similaritySearch(request);

        String context = similarDocuments.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));

        if (context.isEmpty()) {
            return "I don't have enough data to answer that yet. Please wait for the daily jobs to run.";
        }

        String prompt = """
                You are a senior business analyst for a company using the MyDeseret ERP.
                Analyze the following internal data to answer the user's question.
                
                CONTEXT DATA:
                %s
                
                USER QUESTION:
                %s
                
                Provide a professional, data-driven answer. If the data doesn't explain it, say so.
                """.formatted(context, userQuestion);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}