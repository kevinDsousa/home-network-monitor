package com.homenetworkmonitor.scheduler;

import com.homenetworkmonitor.client.AdguardQueryLogClient;
import com.homenetworkmonitor.service.DnsQueryIngestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DnsLogScheduler {

    private final AdguardQueryLogClient queryLogClient;
    private final DnsQueryIngestService ingestService;
    private final int fetchLimit;

    public DnsLogScheduler(AdguardQueryLogClient queryLogClient,
                            DnsQueryIngestService ingestService,
                            @Value("${adguard.querylog-fetch-limit:500}") int fetchLimit) {
        this.queryLogClient = queryLogClient;
        this.ingestService = ingestService;
        this.fetchLimit = fetchLimit;
    }

    @Scheduled(fixedDelayString = "${dnslog.interval-ms:60000}")
    public void run() {
        ingestService.ingest(queryLogClient.fetchRecentQueries(fetchLimit));
    }
}
