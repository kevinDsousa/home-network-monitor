package com.homenetworkmonitor.provider;

import com.homenetworkmonitor.models.dto.DeviceAssociationDTO;

import java.util.List;

public interface DeviceProvider {

    String getName();

    boolean supports(String sourceType);

    List<DeviceAssociationDTO> collect();
}
