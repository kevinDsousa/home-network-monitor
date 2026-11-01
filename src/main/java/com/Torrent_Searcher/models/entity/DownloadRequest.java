package com.Torrent_Searcher.models.entity;

import com.Torrent_Searcher.models.enums.DownloadStatus;
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
@Table(name = "download_request")
public class DownloadRequest {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "search_result_id", nullable = false)
    private SearchResult searchResult;

    private String externalTorrentHash;

    @Enumerated(EnumType.STRING)
    private DownloadStatus status;

    private LocalDateTime requestedAt;

    private LocalDateTime sentAt;
}