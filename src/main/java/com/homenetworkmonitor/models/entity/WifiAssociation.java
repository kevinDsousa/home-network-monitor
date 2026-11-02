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
@Table(name = "wifi_association")
public class WifiAssociation {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "network_device_id", nullable = false)
    private NetworkDevice device;

    @Enumerated(EnumType.STRING)
    private WifiBand band;

    private Integer rssi;

    private Long packetsSent;

    private Long packetsReceived;

    private Double linktimeMinutes;

    private LocalDateTime capturedAt;
}
