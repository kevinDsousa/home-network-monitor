package com.Torrent_Searcher.models.dto;

import com.Torrent_Searcher.models.enums.MediaType;

public record SearchRequestDTO(
        String query,
        MediaType mediaType
) {
}