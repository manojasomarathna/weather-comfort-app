package com.fidenz.weathercomfort.controller;

import com.fidenz.weathercomfort.dto.CacheStatusDTO;
import com.fidenz.weathercomfort.dto.CityWeatherDTO;
import com.fidenz.weathercomfort.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    private final CacheManager cacheManager;

    @GetMapping("/rankings")
    public ResponseEntity<List<CityWeatherDTO>> getRankings() {
        return ResponseEntity.ok(weatherService.getRankedCities());
    }

    @GetMapping("/cache-status")
    public ResponseEntity<List<CacheStatusDTO>> getCacheStatus() {
        List<CacheStatusDTO> statuses = cacheManager.getCacheNames().stream()
                .map(name -> {
                    var cache = cacheManager.getCache(name);
                    long size = 0;
                    if (cache instanceof CaffeineCache caffeineCache) {
                        size = caffeineCache.getNativeCache().estimatedSize();
                    }
                    return CacheStatusDTO.builder()
                            .cacheName(name)
                            .estimatedSize(size)
                            .status(size > 0 ? "HIT" : "MISS")
                            .build();
                })
                .toList();
        return ResponseEntity.ok(statuses);
    }
}
