package com.fidenz.weathercomfort.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fidenz.weathercomfort.dto.CityWeatherDTO;
import com.fidenz.weathercomfort.model.WeatherData;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WeatherService {

    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.api.url}")
    private String apiUrl;

    private final ComfortIndexService comfortIndexService;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    private List<Map<String, Object>> cities;

    @PostConstruct
    void loadCities() throws Exception {
        var resource = new ClassPathResource("cities.json");
        cities = objectMapper.readValue(resource.getInputStream(), new TypeReference<>() {});
    }

    @Cacheable(value = "weatherData", key = "#cityCode")
    public WeatherData fetchWeather(long cityCode) {
        String url = apiUrl + "?id=" + cityCode + "&appid=" + apiKey;
        return restTemplate.getForObject(url, WeatherData.class);
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

            results.add(CityWeatherDTO.builder()
                    .cityName(data.getName())
                    .description(description)
                    .temperatureCelsius(Math.round((data.getMain().getTemp() - 273.15) * 10.0) / 10.0)
                    .humidity(data.getMain().getHumidity())
                    .windSpeed(Math.round(data.getWind().getSpeed() * 3.6 * 10.0) / 10.0)
                    .cloudiness(data.getClouds().getAll())
                    .visibility(data.getVisibility())
                    .comfortScore(score)
                    .build());
        }

        results.sort(Comparator.comparingDouble(CityWeatherDTO::getComfortScore).reversed());
        for (int i = 0; i < results.size(); i++) results.get(i).setRank(i + 1);

        return results;
    }
}
