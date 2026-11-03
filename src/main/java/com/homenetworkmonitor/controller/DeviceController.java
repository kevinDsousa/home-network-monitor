package com.homenetworkmonitor.controller;

import com.homenetworkmonitor.models.dto.NetworkDeviceDTO;
import com.homenetworkmonitor.models.dto.RenameDeviceRequest;
import com.homenetworkmonitor.models.entity.NetworkDevice;
import com.homenetworkmonitor.repository.NetworkDeviceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final NetworkDeviceRepository networkDeviceRepository;

    public DeviceController(NetworkDeviceRepository networkDeviceRepository) {
        this.networkDeviceRepository = networkDeviceRepository;
    }

    @GetMapping
    public List<NetworkDeviceDTO> list() {
        return networkDeviceRepository.findAll().stream()
                .map(DeviceController::toDto)
                .toList();
    }

    @PutMapping("/{mac}/name")
    public ResponseEntity<NetworkDeviceDTO> rename(@PathVariable String mac, @RequestBody RenameDeviceRequest request) {
        return networkDeviceRepository.findByMac(mac.toLowerCase())
                .map(device -> {
                    device.setHostname(request.name());
                    networkDeviceRepository.save(device);
                    return ResponseEntity.ok(toDto(device));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private static NetworkDeviceDTO toDto(NetworkDevice device) {
        return new NetworkDeviceDTO(
                device.getMac(),
                device.getHostname(),
                device.getLastIp(),
                device.getLastBand(),
                device.getLastRssi(),
                device.getLastSeenAt()
        );
    }
}
