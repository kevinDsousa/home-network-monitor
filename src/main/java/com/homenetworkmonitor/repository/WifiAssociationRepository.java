package com.homenetworkmonitor.repository;

import com.homenetworkmonitor.models.entity.WifiAssociation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WifiAssociationRepository extends JpaRepository<WifiAssociation, UUID> {
}
