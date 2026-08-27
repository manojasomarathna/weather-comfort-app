package com.fidenz.weathercomfort.dto;

public class CacheStatusDTO {
    private String cacheName;
    private long estimatedSize;
    private String status;

    public CacheStatusDTO(String cacheName, long estimatedSize, String status) {
        this.cacheName = cacheName;
        this.estimatedSize = estimatedSize;
        this.status = status;
    }

    public String getCacheName() { return cacheName; }
    public long getEstimatedSize() { return estimatedSize; }
    public String getStatus() { return status; }
}
