package com.mydeseret.mydeseret.controller;

import com.mydeseret.mydeseret.service.BusinessIntelligenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ai")
@Tag(name = "AI Analyst", description = "Ask questions about your business data")
public class AIController {

    @Autowired
    private BusinessIntelligenceService aiService;

    @Operation(summary = "Ask the AI a question", description = "Uses RAG to find relevant sales/expense data and generate an answer.")
    @PostMapping("/ask")
    @PreAuthorize("hasAuthority('AI_ASK')")
    public ResponseEntity<Map<String, String>> askAI(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        if (question == null || question.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Question cannot be empty"));
        }

        String answer = aiService.askBusinessQuestion(question);
        return ResponseEntity.ok(Map.of("answer", answer));
    }

    @Operation(summary = "Manually trigger data ingestion", description = "Forces the AI to read the latest data immediately.")
    @PostMapping("/ingest")
    @PreAuthorize("hasAuthority('AI_INGEST')")
    public ResponseEntity<String> triggerIngestion(@RequestBody String content) {
        aiService.ingestData(content);
        return ResponseEntity.ok("Data ingested into Vector Store.");
    }
}