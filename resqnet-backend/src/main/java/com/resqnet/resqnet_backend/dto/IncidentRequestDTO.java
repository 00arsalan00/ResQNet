package com.resqnet.resqnet_backend.dto;

import com.resqnet.resqnet_backend.entity.IncidentType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IncidentRequestDTO {
    @NotNull
    private IncidentType type;

    @NotBlank
    private String reporter;

    private String email;

    private String phoneNumber;

    @NotBlank
    private String description;

    private String address;
    private String street;
    private String landmark;
    
    @NotBlank
    private String city;
    
    @NotBlank
    private String district;
    
    @NotBlank
    private String country;

    @DecimalMin("-90.0") @DecimalMax("90.0")
    private Double latitude;

    @DecimalMin("-180.0") @DecimalMax("180.0")
    private Double longitude;
}
