package com.fidenz.weathercomfort.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fidenz.weathercomfort.dto.CityWeatherDTO;
import com.fidenz.weathercomfort.model.WeatherData;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.api.url}")
    private String apiUrl;

    private final ComfortIndexService comfortIndexService;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    private List<Map<String, Object>> cities;

    public WeatherService(ComfortIndexService comfortIndexService, ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.comfortIndexService = comfortIndexService;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    void loadCities() throws Exception {
        var resource = new ClassPathResource("cities.json");
        cities = objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {});
    }

    private WeatherData fetchWeather(long cityCode) {
        try {
            java.net.URI uri = new java.net.URI(
                "https", "api.openweathermap.org",
                "/data/2.5/weather",
                "id=" + cityCode + "&appid=" + apiKey,
                null
            );
            return restTemplate.getForObject(uri, WeatherData.class);
        } catch (java.net.URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Cacheable("comfortRankings")
    public List<CityWeatherDTO> getRankedCities() {
        List<CityWeatherDTO> results = new ArrayList<>();

        for (Map<String, Object> city : cities) {
            long code = ((Number) city.get("CityCode")).longValue();
            WeatherData data = fetchWeather(code);
            if (data == null) continue;

            double score = comfortIndexService.compute(data);
            String description = (data.getWeather() != null && !data.getWeather().isEmpty())
                    ? data.getWeather().get(0).getDescription()
                    : "N/A";

            double tempC = Math.round((data.getMain().getTemp() - 273.15) * 10.0) / 10.0;
            double windKmh = Math.round(data.getWind().getSpeed() * 3.6 * 10.0) / 10.0;

            results.add(new CityWeatherDTO(
                    data.getName(),
                    description,
                    tempC,
                    data.getMain().getHumidity(),
                    windKmh,
                    data.getClouds().getAll(),
                    data.getVisibility(),
                    score
            ));
        }

        results.sort(Comparator.comparingDouble(CityWeatherDTO::getComfortScore).reversed());
        for (int i = 0; i < results.size(); i++) results.get(i).setRank(i + 1);

        return results;
    }
}
