package com.resqnet.resqnet_backend.service;

import org.springframework.stereotype.Service;

@Service
public class GeocodingServiceImplementation implements GeocodingService {

    @Override
    public double[] getCoordinates(String fullAddress) {
        if (fullAddress.toLowerCase().contains("delhi")) {
            return new double[]{77.2090, 28.6139};
        } else if (fullAddress.toLowerCase().contains("mumbai")) {
            return new double[]{72.8777, 19.0760};
        } else if (fullAddress.toLowerCase().contains("london")) {
            return new double[]{-0.1278, 51.5074};
        } else if (fullAddress.toLowerCase().contains("new york")) {
            return new double[]{-74.0060, 40.7128};
        }
        
        return new double[]{0.0, 0.0};
    }
}
