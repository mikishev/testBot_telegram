package ru.testBot.telegram;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Weather cache.
 */
@Slf4j
public class WeatherCache {

    private final Map<String, WeatherInfo> cache = new HashMap<>();
    private WeatherProvider weatherProvider;

    /**
     * Default constructor.
     */
    public WeatherCache() {
    }

    public void setWeatherProvider(WeatherProvider weatherProvider) {
        this.weatherProvider = weatherProvider;
    }

    /**
     * Get ACTUAL weather info for current city or null if current city not found.
     * If cache doesn't contain weather info OR contains NOT ACTUAL info then we should download info
     * If you download weather info then you should set expiry time now() plus 5 minutes.
     * If you can't download weather info then remove weather info for current city from cache.
     *
     * @param city - city
     * @return actual weather info
     */
    public WeatherInfo getWeatherInfo(String city) {
        WeatherInfo result = new WeatherInfo();
        try {
            result = weatherProvider.get(city);
            if (cache.get(city) == null) {
                cache.put(city, result);
                log.info("Weather info for " + city + " has been downloaded!");
            } else if (result.getExpiryTime().compareTo(LocalDateTime.now().plusHours(1)) > 0) {
                cache.put(city, result);
                log.info("Fresh weather info for " + city + " has been downloaded!");
            }
        } catch (RuntimeException ex) {
            cache.remove(city);
            log.info("Weather info for " + city + " has been removed!");

        }

        return cache.get(city);
    }

    /**
     * Remove weather info from cache.
     **/
    public void removeWeatherInfo(String city) {
        cache.remove(city);
    }
}
