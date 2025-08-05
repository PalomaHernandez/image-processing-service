package com.project.image_service.services;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    private final Map<Long, Bucket> buckets = new ConcurrentHashMap<>();

    public boolean tryConsume(Long userId) {
        Bucket bucket = buckets.computeIfAbsent(userId, this::newBucket);
        return bucket.tryConsume(1);
    }

    private Bucket newBucket(Long userId) {
        // 5 transformaciones por minuto
        Refill refill = Refill.greedy(5, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(5, refill);
        return Bucket.builder().addLimit(limit).build();
    }
}
