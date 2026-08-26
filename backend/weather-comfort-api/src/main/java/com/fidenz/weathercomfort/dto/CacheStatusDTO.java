package com.fidenz.weathercomfort.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CacheStatusDTO {
    private String cacheName;
    private long estimatedSize;
    private String status; // HIT or MISS
}
