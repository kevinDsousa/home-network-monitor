package com.Torrent_Searcher.models.entity;

import com.Torrent_Searcher.models.enums.MediaType;
import com.Torrent_Searcher.models.enums.ProviderType;
import com.Torrent_Searcher.models.enums.SearchStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "search_execution")
public class SearchExecution {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private String query;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @Enumerated(EnumType.STRING)
    private SearchStatus status;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;
}