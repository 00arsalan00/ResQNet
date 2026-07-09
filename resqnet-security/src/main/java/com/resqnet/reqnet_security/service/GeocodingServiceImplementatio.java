package com.resqnet.reqnet_security.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.resqnet.resqnet_backend.service.GeocodingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

public class GeocodingServiceImplementatio implements GeocodingService {

    private final RestTemplate restTemplate;

    @Value("${geocoding.provider.url}")
    private String apiUrl;

    @Override
    public double[] getCoordinates(String address) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent","ResQNet-App");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<JsonNode[]> response = new restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    entity,
                    JsonNode[].class,
                    address
            );

            if(response.getBody()!=null && response.getBody().length>0){
                JsonNode jsonNode = response.getBody()[0];
                double latitude = jsonNode.get("latitude").asDouble();
                double longitude = jsonNode.get("longitude").asDouble();
                return new double[]{latitude,longitude};
            }
        }catch (Exception e){
            return new double[]{0.0,0.0};
        }

        return new double[]{0.0,0.0};

    }

}
