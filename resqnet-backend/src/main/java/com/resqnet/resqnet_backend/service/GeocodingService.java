package com.resqnet.resqnet_backend.service;

public interface GeocodingService {
    double[] getCoordinates(String fullAddress);
}
