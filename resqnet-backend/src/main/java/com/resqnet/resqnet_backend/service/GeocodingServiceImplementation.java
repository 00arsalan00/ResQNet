package com.resqnet.resqnet_backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GeocodingServiceImplementation implements GeocodingService {

    private final RestTemplate restTemplate;

    @Value("${geocoding.provider.url:https://nominatim.openstreetmap.org/search?q={address}&format=json&limit=1}")
    private String apiUrl;

    @Override
    public double[] getCoordinates(String fullAddress) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "ResQNet-App");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<JsonNode[]> response = restTemplate.exchange(
                apiUrl, 
                HttpMethod.GET, 
                entity, 
                JsonNode[].class, 
                fullAddress
            );

            if (response.getBody() != null && response.getBody().length > 0) {
                JsonNode result = response.getBody()[0];
                double lat = result.get("lat").asDouble();
                double lon = result.get("lon").asDouble();
                return new double[]{lon, lat};
            }
        } catch (Exception e) {
            return new double[]{0.0, 0.0};
        }
        return new double[]{0.0, 0.0};
    }
}
