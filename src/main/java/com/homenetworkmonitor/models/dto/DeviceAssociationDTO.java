package com.homenetworkmonitor.models.dto;

import com.homenetworkmonitor.models.enums.WifiBand;

public record DeviceAssociationDTO(
        String mac,
        String hostname,
        String ip,
        WifiBand band,
        Integer rssi,
        Long packetsSent,
        Long packetsReceived,
        Double linktimeMinutes
) {
}
