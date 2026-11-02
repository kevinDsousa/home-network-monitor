package com.homenetworkmonitor.controller;

import com.homenetworkmonitor.models.dto.DnsActivityDTO;
import com.homenetworkmonitor.models.entity.DnsQueryEntry;
import com.homenetworkmonitor.repository.DnsQueryEntryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DnsActivityController {

    private final DnsQueryEntryRepository dnsQueryEntryRepository;

    public DnsActivityController(DnsQueryEntryRepository dnsQueryEntryRepository) {
        this.dnsQueryEntryRepository = dnsQueryEntryRepository;
    }

    @GetMapping("/api/dns-activity")
    public List<DnsActivityDTO> recentActivity(@RequestParam(required = false) String mac) {
        List<DnsQueryEntry> entries = (mac == null || mac.isBlank())
                ? dnsQueryEntryRepository.findTop200ByOrderByQueriedAtDesc()
                : dnsQueryEntryRepository.findTop200ByDevice_MacOrderByQueriedAtDesc(mac);

        return entries.stream()
                .map(entry -> new DnsActivityDTO(
                        entry.getClientIp(),
                        entry.getDevice() != null ? entry.getDevice().getMac() : null,
                        entry.getDevice() != null ? entry.getDevice().getHostname() : null,
                        entry.getDomain(),
                        entry.isBlocked(),
                        entry.getQueriedAt()
                ))
                .toList();
    }
}
