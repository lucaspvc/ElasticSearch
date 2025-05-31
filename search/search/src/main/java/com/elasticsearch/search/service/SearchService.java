package com.elasticsearch.search.service;

import co.elastic.clients.elasticsearch.core.search.Hit;
import com.elasticsearch.search.api.model.Result;
import com.elasticsearch.search.api.model.Results;
import com.elasticsearch.search.domain.EsClient;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private final EsClient esClient;

    public SearchService(EsClient esClient) {
        this.esClient = esClient;
    }

    public Results search(String query, Integer page, Integer charLimit, Integer itemsPerPage) {
        int safePage = (page != null) ? page : 1;
        int safeCharLimit = (charLimit != null) ? charLimit : 300;
        int safeItemsPerPage = (itemsPerPage != null && (itemsPerPage == 10 || itemsPerPage == 20 || itemsPerPage == 30))
                ? itemsPerPage
                : 10;

        var searchResponse = esClient.search(query, safePage, safeCharLimit, safeItemsPerPage);
        List<Hit<ObjectNode>> hits = searchResponse.hits().hits();

        Results results = new Results();
        results.setPage((long) safePage);
        results.setTotalResults(searchResponse.hits().total().value());
        results.setItemsPerPage((long) safeItemsPerPage);

        if (hits.isEmpty()) {
            List<String> suggestions = esClient.getSuggestions(query);
            results.setSuggestions(suggestions);
            results.setResultsList(Collections.emptyList());
        } else {
            var resultsList = hits.stream().map(h -> {
                String rawContent = h.source().get("content").asText();
                List<String> realces = h.highlight() != null ? h.highlight().get("content") : null;
                String highlight;

                if (realces != null && realces.size() > 1) {
                    // Vários trechos: junta os fragments destacados
                    highlight = String.join(" ... ", realces);
                } else {
                    // Um ou nenhum trecho: destaca manualmente no texto original
                    String queryTerm = query; // ou parse para múltiplas palavras, se quiser
                    highlight = rawContent.replaceAll("(?i)(" + java.util.regex.Pattern.quote(queryTerm) + ")", "<mark>$1</mark>");
                }


                return new Result()
                        .content(rawContent)
                        .highlightedContent(highlight)
                        .title(h.source().get("title").asText())
                        .url(h.source().get("url").asText());
            }).collect(Collectors.toList());

            results.setResultsList(resultsList);
        }

        return results;
    }
}
