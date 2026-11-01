package com.Torrent_Searcher.models.entity;

import com.Torrent_Searcher.models.enums.ProviderType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "search_source")
public class SearchSource {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String nome;

    private String baseUrl;

    private ProviderType providerType;

    private Boolean enabled;

    private Integer priority;

    private String Configuration;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
