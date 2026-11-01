package com.Torrent_Searcher.agent;

import com.Torrent_Searcher.provider.SearchProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchAgent {

    private final List<SearchProvider> providers;

    public SearchAgent(List<SearchProvider> providers) {
        this.providers = providers;
    }

    public void search(String query) {

        for (SearchProvider provider : providers) {

            System.out.println("Consultando: " + provider.getName());

            var results = provider.search(query);

            results.forEach(System.out::println);
        }
    }
}