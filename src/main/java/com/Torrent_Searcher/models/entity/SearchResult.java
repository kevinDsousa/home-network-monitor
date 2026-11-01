package com.Torrent_Searcher.models.entity;

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
@Table(name = "search_result")
public class SearchResult {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "search_execution_id", nullable = false)
    private SearchExecution searchExecution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "search_source_id", nullable = false)
    private SearchSource searchSource;

    private String title;

    @Column(length = 2048)
    private String magnetUri;

    @Column(length = 2048)
    private String torrentUrl;

    private Long sizeBytes;

    private Integer seeders;

    private Integer leechers;

    private String quality;

    private LocalDateTime publishedAt;
}