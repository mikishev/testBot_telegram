package ru.testBot.telegram;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

public class WeatherProvider {

    private RestTemplate restTemplate;
    private String appKey;

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    /**
     * Download ACTUAL weather info from internet.
     * You should call GET http://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}
     * You should use Spring Rest Template for calling requests
     *
     * @param city - city
     * @return weather info or null
     */
    public WeatherInfo get(String city) {
        String weatherResourceUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + appKey;
        WeatherInfo weatherInfo = new WeatherInfo();

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(weatherResourceUrl, String.class);

            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                JSONObject json = new JSONObject(responseEntity.getBody());
                weatherInfo = new WeatherInfo();
                weatherInfo.setCity(city);
                weatherInfo.setShortDescription(json.getJSONArray("weather").getJSONObject(0).getString("main"));
                weatherInfo.setDescription(json.getJSONArray("weather").getJSONObject(0).getString("description"));
                weatherInfo.setTemperature(json.getJSONObject("main").getDouble("temp"));
                weatherInfo.setFeelsLikeTemperature(json.getJSONObject("main").getDouble("feels_like"));
                weatherInfo.setWindSpeed(json.getJSONObject("wind").getDouble("speed"));
                weatherInfo.setPressure(json.getJSONObject("main").getDouble("pressure"));
                weatherInfo.setExpiryTime(LocalDateTime.now());
                return weatherInfo;
            }
        } catch (Exception ex) {
            throw new RuntimeException("disconnected" + ex.getMessage());
        }
        return weatherInfo;
    }
}
