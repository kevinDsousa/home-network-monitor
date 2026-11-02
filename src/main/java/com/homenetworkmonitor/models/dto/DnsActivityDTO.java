package com.homenetworkmonitor.models.dto;

import java.time.LocalDateTime;

public record DnsActivityDTO(
        String clientIp,
        String mac,
        String deviceName,
        String domain,
        boolean blocked,
        LocalDateTime queriedAt
) {
}
