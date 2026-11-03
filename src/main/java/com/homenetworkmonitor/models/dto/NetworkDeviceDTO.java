package com.homenetworkmonitor.models.dto;

import com.homenetworkmonitor.models.enums.WifiBand;

import java.time.LocalDateTime;

public record NetworkDeviceDTO(
        String mac,
        String hostname,
        String lastIp,
        WifiBand lastBand,
        Integer lastRssi,
        LocalDateTime lastSeenAt
) {
}
