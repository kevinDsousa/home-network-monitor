package com.Torrent_Searcher.provider;

import com.Torrent_Searcher.models.dto.ProviderResultDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HtmlSearchProvider implements SearchProvider {

    @Override
    public String getName() {
        return "HTML Provider";
    }

    @Override
    public boolean supports(String sourceType) {
        return "HTML".equalsIgnoreCase(sourceType);
    }

    @Override
    public List<ProviderResultDTO> search(String query) {
        // Futuramente:
        // 1. Acessar a página configurada.
        // 2. Interpretar o HTML.
        // 3. Extrair os resultados.
        // 4. Converter para ProviderResultDTO.

        return List.of();
    }
}