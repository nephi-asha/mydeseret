package com.mydeseret.mydeseret.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitingService {

    // Cache to store buckets for each Tenant or User IP
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    /**
     * Gets (or creates) a bucket for a specific key (Tenant ID or API Key).
     * Policy: 100 requests per minute.
     */
    public Bucket resolveBucket(String key) {
        return cache.computeIfAbsent(key, this::newBucket);
    }

    private Bucket newBucket(String key) {
        // Capacity: 100 tokens total
        // Refill: 100 tokens every 1 minute
        Bandwidth limit = Bandwidth.classic(100, Refill.greedy(100, Duration.ofMinutes(1)));
        
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
    
    // Stricter limit for AI endpoints since it is Expensive.
    public Bucket resolveAiBucket(String key) {
        return cache.computeIfAbsent("AI_" + key, k -> {
             // Only 10 AI requests per minute
             Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
             return Bucket.builder().addLimit(limit).build();
        });
    }
}