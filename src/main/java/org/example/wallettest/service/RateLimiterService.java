package org.example.wallettest.service;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private final Map<UUID, Bucket> walletBuckets = new ConcurrentHashMap<>();

    public boolean tryConsume(UUID walletId) {
        Bucket bucket = walletBuckets.computeIfAbsent(walletId, id -> createBucket());
        return bucket.tryConsume(1);
    }

    private Bucket createBucket() {
        Bandwidth limit = Bandwidth.simple(1000, Duration.ofSeconds(1));
        return Bucket4j.builder().addLimit(limit).build();
    }
}

