package com.homenetworkmonitor.agent;

import com.homenetworkmonitor.provider.DeviceProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeviceCollectorAgent {

    private final List<DeviceProvider> providers;

    public DeviceCollectorAgent(List<DeviceProvider> providers) {
        this.providers = providers;
    }

    public void collectAll() {

        for (DeviceProvider provider : providers) {

            System.out.println("Coletando: " + provider.getName());

            var devices = provider.collect();

            devices.forEach(System.out::println);
        }
    }
}
