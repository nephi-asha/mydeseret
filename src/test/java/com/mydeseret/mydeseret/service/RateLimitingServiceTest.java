package com.mydeseret.mydeseret.service;

import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RateLimitingServiceTest {

    private RateLimitingService rateLimitingService;

    @BeforeEach
    void setUp() {
        rateLimitingService = new RateLimitingService();
    }

    @Test
    void resolveBucket_ShouldReturnSameBucketForKey() {
        String key = "test-user";
        Bucket bucket1 = rateLimitingService.resolveBucket(key);
        Bucket bucket2 = rateLimitingService.resolveBucket(key);

        assertNotNull(bucket1);
        assertSame(bucket1, bucket2, "Should return the same bucket instance for the same key");
    }

    @Test
    void resolveBucket_ShouldHaveCorrectCapacity() {
        String key = "test-user";
        Bucket bucket = rateLimitingService.resolveBucket(key);

        // Capacity is 100
        assertEquals(100, bucket.getAvailableTokens());
    }

    @Test
    void resolveAiBucket_ShouldHaveStricterLimit() {
        String key = "test-user";
        Bucket bucket = rateLimitingService.resolveAiBucket(key);

        // Capacity is 10
        assertEquals(10, bucket.getAvailableTokens());
    }

    @Test
    void resolveAiBucket_ShouldBeDistinctFromRegularBucket() {
        String key = "test-user";
        Bucket regularBucket = rateLimitingService.resolveBucket(key);
        Bucket aiBucket = rateLimitingService.resolveAiBucket(key);

        assertNotSame(regularBucket, aiBucket, "AI bucket should be different from regular bucket");
    }
}
