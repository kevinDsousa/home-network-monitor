package com.Torrent_Searcher.models.dto;

public record SearchResultDTO(
        String title,
        String source,
        String url,
        String magnetUri,
        Long sizeBytes,
        Integer seeders,
        Integer leechers
) {
}