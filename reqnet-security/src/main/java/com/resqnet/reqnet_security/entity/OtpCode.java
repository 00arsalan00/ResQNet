package com.resqnet.reqnet_security.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "otp_codes")
@Entity
public class OtpCode {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false,updatable = false)
    private LocalDateTime expiryTime;

    @Builder.Default
    private boolean used=false;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryTime);
    }
}
