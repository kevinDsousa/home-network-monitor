package com.homenetworkmonitor.service;

import com.homenetworkmonitor.models.dto.DeviceAssociationDTO;
import com.homenetworkmonitor.models.entity.NetworkDevice;
import com.homenetworkmonitor.models.entity.WifiAssociation;
import com.homenetworkmonitor.repository.NetworkDeviceRepository;
import com.homenetworkmonitor.repository.WifiAssociationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeviceInventoryService {

    private final NetworkDeviceRepository deviceRepository;
    private final WifiAssociationRepository wifiAssociationRepository;

    public DeviceInventoryService(NetworkDeviceRepository deviceRepository,
                                   WifiAssociationRepository wifiAssociationRepository) {
        this.deviceRepository = deviceRepository;
        this.wifiAssociationRepository = wifiAssociationRepository;
    }

    @Transactional
    public void persist(List<DeviceAssociationDTO> associations) {
        LocalDateTime now = LocalDateTime.now();

        for (DeviceAssociationDTO dto : associations) {
            NetworkDevice device = deviceRepository.findByMac(dto.mac())
                    .orElseGet(() -> {
                        NetworkDevice created = new NetworkDevice();
                        created.setMac(dto.mac());
                        created.setFirstSeenAt(now);
                        return created;
                    });

            if (dto.hostname() != null && !dto.hostname().isBlank() && !"unknow".equalsIgnoreCase(dto.hostname())) {
                device.setHostname(dto.hostname());
            }
            device.setLastIp(dto.ip());
            device.setLastBand(dto.band());
            device.setLastRssi(dto.rssi());
            device.setLastSeenAt(now);
            deviceRepository.save(device);

            WifiAssociation association = new WifiAssociation();
            association.setDevice(device);
            association.setBand(dto.band());
            association.setRssi(dto.rssi());
            association.setPacketsSent(dto.packetsSent());
            association.setPacketsReceived(dto.packetsReceived());
            association.setLinktimeMinutes(dto.linktimeMinutes());
            association.setCapturedAt(now);
            wifiAssociationRepository.save(association);
        }
    }
}
