package com.homenetworkmonitor.models.dto;

import java.time.LocalDateTime;

public record DnsQueryDTO(
        String clientIp,
        String domain,
        boolean blocked,
        LocalDateTime queriedAt
) {
}
