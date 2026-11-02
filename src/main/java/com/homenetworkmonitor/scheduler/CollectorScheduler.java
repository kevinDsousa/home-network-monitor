package com.homenetworkmonitor.scheduler;

import com.homenetworkmonitor.agent.DeviceCollectorAgent;
import com.homenetworkmonitor.service.DeviceInventoryService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CollectorScheduler {

    private final DeviceCollectorAgent collectorAgent;
    private final DeviceInventoryService inventoryService;

    public CollectorScheduler(DeviceCollectorAgent collectorAgent, DeviceInventoryService inventoryService) {
        this.collectorAgent = collectorAgent;
        this.inventoryService = inventoryService;
    }

    @Scheduled(fixedDelayString = "${collector.interval-ms:300000}")
    public void run() {
        inventoryService.persist(collectorAgent.collectAll());
    }
}
