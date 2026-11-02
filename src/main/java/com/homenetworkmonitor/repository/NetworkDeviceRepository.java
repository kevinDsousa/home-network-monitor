package com.homenetworkmonitor.repository;

import com.homenetworkmonitor.models.entity.NetworkDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NetworkDeviceRepository extends JpaRepository<NetworkDevice, UUID> {

    Optional<NetworkDevice> findByMac(String mac);
}
