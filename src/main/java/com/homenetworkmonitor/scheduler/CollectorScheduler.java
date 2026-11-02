package com.homenetworkmonitor.scheduler;

import com.homenetworkmonitor.agent.DeviceCollectorAgent;
import com.homenetworkmonitor.service.DeviceInventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CollectorScheduler {

    private static final Logger log = LoggerFactory.getLogger(CollectorScheduler.class);

    private final DeviceCollectorAgent collectorAgent;
    private final DeviceInventoryService inventoryService;

    public CollectorScheduler(DeviceCollectorAgent collectorAgent, DeviceInventoryService inventoryService) {
        this.collectorAgent = collectorAgent;
        this.inventoryService = inventoryService;
    }

    @Scheduled(fixedDelayString = "${collector.interval-ms:300000}")
    public void run() {
        var devices = collectorAgent.collectAll();
        inventoryService.persist(devices);
        log.info("Coleta de dispositivos concluída: {} dispositivos", devices.size());
    }
}
