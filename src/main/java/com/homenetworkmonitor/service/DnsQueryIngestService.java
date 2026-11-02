package com.homenetworkmonitor.service;

import com.homenetworkmonitor.models.dto.DnsQueryDTO;
import com.homenetworkmonitor.models.entity.DnsQueryEntry;
import com.homenetworkmonitor.models.entity.NetworkDevice;
import com.homenetworkmonitor.repository.DnsQueryEntryRepository;
import com.homenetworkmonitor.repository.NetworkDeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DnsQueryIngestService {

    private final DnsQueryEntryRepository dnsQueryEntryRepository;
    private final NetworkDeviceRepository deviceRepository;

    public DnsQueryIngestService(DnsQueryEntryRepository dnsQueryEntryRepository,
                                  NetworkDeviceRepository deviceRepository) {
        this.dnsQueryEntryRepository = dnsQueryEntryRepository;
        this.deviceRepository = deviceRepository;
    }

    @Transactional
    public void ingest(List<DnsQueryDTO> queries) {
        LocalDateTime lastSeen = dnsQueryEntryRepository.findTopByOrderByQueriedAtDesc()
                .map(DnsQueryEntry::getQueriedAt)
                .orElse(null);

        Map<String, NetworkDevice> deviceByIp = new HashMap<>();
        for (NetworkDevice device : deviceRepository.findAll()) {
            if (device.getLastIp() != null) {
                deviceByIp.put(device.getLastIp(), device);
            }
        }

        for (DnsQueryDTO dto : queries) {
            if (lastSeen != null && !dto.queriedAt().isAfter(lastSeen)) {
                continue;
            }

            DnsQueryEntry entry = new DnsQueryEntry();
            entry.setDevice(deviceByIp.get(dto.clientIp()));
            entry.setClientIp(dto.clientIp());
            entry.setDomain(dto.domain());
            entry.setBlocked(dto.blocked());
            entry.setQueriedAt(dto.queriedAt());
            dnsQueryEntryRepository.save(entry);
        }
    }
}
