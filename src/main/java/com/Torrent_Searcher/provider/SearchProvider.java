package com.Torrent_Searcher.provider;

import com.Torrent_Searcher.models.dto.ProviderResultDTO;

import java.util.List;

public interface SearchProvider {

    String getName();

    boolean supports(String sourceType);

    List<ProviderResultDTO> search(String query);
}