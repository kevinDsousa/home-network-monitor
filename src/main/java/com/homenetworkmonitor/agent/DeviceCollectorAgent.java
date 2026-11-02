package com.homenetworkmonitor.agent;

import com.homenetworkmonitor.models.dto.DeviceAssociationDTO;
import com.homenetworkmonitor.provider.DeviceProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DeviceCollectorAgent {

    private final List<DeviceProvider> providers;

    public DeviceCollectorAgent(List<DeviceProvider> providers) {
        this.providers = providers;
    }

    public List<DeviceAssociationDTO> collectAll() {
        List<DeviceAssociationDTO> result = new ArrayList<>();
        for (DeviceProvider provider : providers) {
            result.addAll(provider.collect());
        }
        return result;
    }
}
