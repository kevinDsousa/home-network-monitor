package com.Torrent_Searcher.provider;

import com.Torrent_Searcher.models.dto.ProviderResultDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiSearchProvider implements SearchProvider {

    @Override
    public String getName() {
        return "API Provider";
    }

    @Override
    public boolean supports(String sourceType) {
        return "API".equalsIgnoreCase(sourceType);
    }

    @Override
    public List<ProviderResultDTO> search(String query) {
        // Futuramente:
        // 1. Montar a requisição HTTP.
        // 2. Consultar a API.
        // 3. Interpretar o JSON.
        // 4. Converter para ProviderResultDTO.

        return List.of();
    }
}