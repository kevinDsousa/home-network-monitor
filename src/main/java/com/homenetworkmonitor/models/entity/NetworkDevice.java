package com.homenetworkmonitor.models.entity;

import com.homenetworkmonitor.models.enums.WifiBand;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "network_device")
public class NetworkDevice {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, unique = true)
    private String mac;

    private String hostname;

    private String lastIp;

    private String vendor;

    @Enumerated(EnumType.STRING)
    private WifiBand lastBand;

    private Integer lastRssi;

    private LocalDateTime firstSeenAt;

    private LocalDateTime lastSeenAt;
}
