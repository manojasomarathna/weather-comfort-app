package com.fidenz.weathercomfort.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CityWeatherDTO {
    private int rank;
    private String cityName;
    private String description;
    private double temperatureCelsius;
    private int humidity;
    private double windSpeed;
    private int cloudiness;
    private int visibility;
    private double comfortScore;
}
