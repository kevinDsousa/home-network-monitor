package com.homenetworkmonitor.repository;

import com.homenetworkmonitor.models.entity.DnsQueryEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DnsQueryEntryRepository extends JpaRepository<DnsQueryEntry, UUID> {

    Optional<DnsQueryEntry> findTopByOrderByQueriedAtDesc();

    List<DnsQueryEntry> findTop200ByOrderByQueriedAtDesc();

    List<DnsQueryEntry> findTop200ByDevice_MacOrderByQueriedAtDesc(String mac);
}
