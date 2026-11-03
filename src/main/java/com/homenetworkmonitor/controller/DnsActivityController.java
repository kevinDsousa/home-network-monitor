package com.homenetworkmonitor.controller;

import com.homenetworkmonitor.models.dto.DnsActivityDTO;
import com.homenetworkmonitor.models.entity.NetworkDevice;
import com.homenetworkmonitor.repository.DnsQueryEntryRepository;
import com.homenetworkmonitor.repository.NetworkDeviceRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DnsActivityController {

    private final DnsQueryEntryRepository dnsQueryEntryRepository;
    private final NetworkDeviceRepository networkDeviceRepository;

    public DnsActivityController(DnsQueryEntryRepository dnsQueryEntryRepository,
                                  NetworkDeviceRepository networkDeviceRepository) {
        this.dnsQueryEntryRepository = dnsQueryEntryRepository;
        this.networkDeviceRepository = networkDeviceRepository;
    }

    @GetMapping("/api/dns-activity")
    public List<DnsActivityDTO> recentActivity(@RequestParam(required = false) String mac) {
        Map<String, NetworkDevice> deviceByIp = new HashMap<>();
        for (NetworkDevice device : networkDeviceRepository.findAll()) {
            if (device.getLastIp() != null) {
                deviceByIp.put(device.getLastIp(), device);
            }
        }

        return dnsQueryEntryRepository.findTop200ByOrderByQueriedAtDesc().stream()
                .map(entry -> {
                    NetworkDevice device = deviceByIp.get(entry.getClientIp());
                    return new DnsActivityDTO(
                            entry.getClientIp(),
                            device != null ? device.getMac() : null,
                            device != null ? device.getHostname() : null,
                            entry.getDomain(),
                            entry.isBlocked(),
                            entry.getQueriedAt()
                    );
                })
                .filter(dto -> mac == null || mac.isBlank() || mac.equalsIgnoreCase(dto.mac()))
                .toList();
    }
}
