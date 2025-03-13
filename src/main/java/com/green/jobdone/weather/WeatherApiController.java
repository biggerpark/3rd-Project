package com.green.jobdone.weather;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("weather")
@Tag(name="날씨 API")
public class WeatherApiController {

    @Value("${weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @GetMapping
    @Operation(summary = "지역 입력하면 현재 날씨와 미래 5일 날씨 알려줌")
    public ResponseEntity<String> getWeather(@RequestParam String location) {
        String geocodeUrl = String.format("http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s",
                location, apiKey);

        try {
            ResponseEntity<String> geocodeResponse = restTemplate.getForEntity(geocodeUrl, String.class);
            if (geocodeResponse.getStatusCode() != HttpStatus.OK || geocodeResponse.getBody() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("위치 정보를 찾을 수 없습니다.");
            }

            JsonNode geocodeJsonArray = objectMapper.readTree(geocodeResponse.getBody());
            if (geocodeJsonArray.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("위치 정보를 찾을 수 없습니다.");
            }

            JsonNode locationJson = geocodeJsonArray.get(0);
            double lat = locationJson.get("lat").asDouble();
            double lon = locationJson.get("lon").asDouble();

            // 5일 날씨 예보 URL
            String forecastUrl = String.format("https://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&appid=%s&units=metric&lang=kr",
                    lat, lon, apiKey);

            String forecastResponse = restTemplate.getForObject(forecastUrl, String.class);
            return ResponseEntity.ok(forecastResponse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("날씨 정보를 가져오는 데 실패했습니다.");
        }
    }
}

