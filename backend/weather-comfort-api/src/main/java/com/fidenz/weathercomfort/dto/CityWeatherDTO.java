package com.fidenz.weathercomfort.dto;

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

    public CityWeatherDTO() {}

    public CityWeatherDTO(String cityName, String description, double temperatureCelsius,
                          int humidity, double windSpeed, int cloudiness, int visibility, double comfortScore) {
        this.cityName = cityName;
        this.description = description;
        this.temperatureCelsius = temperatureCelsius;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.cloudiness = cloudiness;
        this.visibility = visibility;
        this.comfortScore = comfortScore;
    }

    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getTemperatureCelsius() { return temperatureCelsius; }
    public void setTemperatureCelsius(double temperatureCelsius) { this.temperatureCelsius = temperatureCelsius; }
    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }
    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }
    public int getCloudiness() { return cloudiness; }
    public void setCloudiness(int cloudiness) { this.cloudiness = cloudiness; }
    public int getVisibility() { return visibility; }
    public void setVisibility(int visibility) { this.visibility = visibility; }
    public double getComfortScore() { return comfortScore; }
    public void setComfortScore(double comfortScore) { this.comfortScore = comfortScore; }
}
