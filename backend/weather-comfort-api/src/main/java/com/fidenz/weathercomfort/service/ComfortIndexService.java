package com.fidenz.weathercomfort.service;

import com.fidenz.weathercomfort.model.WeatherData;
import org.springframework.stereotype.Service;

@Service
public class ComfortIndexService {

    /**
     * Comfort Index Formula:
     *   Score = (tempScore * 0.35) + (humidityScore * 0.25) + (windScore * 0.20)
     *         + (visibilityScore * 0.10) + (cloudScore * 0.10)
     *
     * - Temperature (35%): Ideal 22°C. Penalty increases with deviation.
     * - Humidity    (25%): Ideal 40–60%. Outside range reduces comfort.
     * - Wind Speed  (20%): Ideal 5–15 km/h. Too calm or too windy is uncomfortable.
     * - Visibility  (10%): Max 10000m = 100 score.
     * - Cloudiness  (10%): 0–30% cloud = comfortable, 100% = 0 score.
     */
    public double compute(WeatherData data) {
        double tempC = data.getMain().getTemp() - 273.15;
        int humidity = data.getMain().getHumidity();
        double windKmh = data.getWind().getSpeed() * 3.6;
        int visibility = Math.min(data.getVisibility(), 10000);
        int clouds = data.getClouds().getAll();

        double tempScore = Math.max(0, 100 - (Math.abs(tempC - 22) * 4));
        double humidityScore = (humidity >= 40 && humidity <= 60)
                ? 100
                : Math.max(0, 100 - (Math.abs(humidity - 50) * 2));
        double windScore = (windKmh >= 5 && windKmh <= 15)
                ? 100
                : Math.max(0, 100 - (Math.abs(windKmh - 10) * 3));
        double visibilityScore = (visibility / 10000.0) * 100;
        double cloudScore = Math.max(0, 100 - clouds);

        double score = (tempScore * 0.35)
                + (humidityScore * 0.25)
                + (windScore * 0.20)
                + (visibilityScore * 0.10)
                + (cloudScore * 0.10);

        return Math.round(score * 100.0) / 100.0;
    }
}
