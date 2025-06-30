package com.elasticsearch.search.service;

import co.elastic.clients.elasticsearch.core.search.Hit;
import com.elasticsearch.search.api.model.Result;
import com.elasticsearch.search.api.model.Results;
import com.elasticsearch.search.domain.EsClient;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

                // --- Extrai termos da query (aspas e termos soltos) ---
                Pattern quotedPattern = Pattern.compile("\"([^\"]+)\"");
                Matcher matcher = quotedPattern.matcher(query);

                List<String> quotedTerms = new ArrayList<>();
                while (matcher.find()) {
                    quotedTerms.add(matcher.group(1));
                }

                String cleanedQuery = query;
                for (String qt : quotedTerms) {
                    cleanedQuery = cleanedQuery.replace("\"" + qt + "\"", "");
                }

                List<String> allTerms = new ArrayList<>(quotedTerms);
                for (String term : cleanedQuery.trim().split("\\s+")) {
                    if (!term.isBlank()) {
                        allTerms.add(term);
                    }
                }
                // -------------------------------------------------------

                if (realces != null && !realces.isEmpty()) {
                    String joined = String.join(" ... ", realces);
                    String plain = joined.replaceAll("</?mark>", "");

                    if (plain.length() > safeCharLimit) {
                        plain = plain.substring(0, safeCharLimit);
                    }

                    for (String term : allTerms) {
                        plain = plain.replaceAll("(?i)\\b(" + Pattern.quote(term) + ")\\b", "<mark>$1</mark>");
                    }

                    highlight = plain;
                } else {
                    String plain = rawContent.length() > safeCharLimit
                            ? rawContent.substring(0, safeCharLimit)
                            : rawContent;

                    for (String term : allTerms) {
                        plain = plain.replaceAll("(?i)\\b(" + Pattern.quote(term) + ")\\b", "<mark>$1</mark>");
                    }

                    highlight = plain;
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
