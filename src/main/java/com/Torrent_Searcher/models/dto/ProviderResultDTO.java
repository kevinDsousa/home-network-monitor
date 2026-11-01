package com.Torrent_Searcher.models.dto;

public record ProviderResultDTO(
        String title,
        String url,
        String magnetUri,
        Long sizeBytes,
        Integer seeders,
        Integer leechers
) {
}