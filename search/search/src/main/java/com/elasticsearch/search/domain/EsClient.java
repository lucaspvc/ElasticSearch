package com.elasticsearch.search.domain;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SuggestMode;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.altindag.ssl.SSLFactory;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EsClient {
    private ElasticsearchClient elasticsearchClient;

    public EsClient() {
        creatConection();
    }

    private void creatConection() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        String USER = "elastic";
        String PWD = "user123";

        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(USER, PWD));

        SSLFactory sslFactory = SSLFactory.builder().withUnsafeTrustMaterial().withUnsafeHostnameVerifier().build();

        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200, "https"))
                .setHttpClientConfigCallback(
                        (HttpAsyncClientBuilder httpClientBuilder) -> httpClientBuilder
                                .setDefaultCredentialsProvider(credentialsProvider)
                                .setSSLContext(sslFactory.getSslContext())
                                .setSSLHostnameVerifier(sslFactory.getHostnameVerifier())
                ).build();

        ElasticsearchTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper()
        );

        elasticsearchClient = new co.elastic.clients.elasticsearch.ElasticsearchClient(transport);
    }

    public SearchResponse search(String query, Integer page, Integer cl, Integer itemsPerPage) {
        int currentPage = (page != null && page > 0) ? page : 1;
        int safeItemsPerPage = (itemsPerPage != null && (itemsPerPage == 10 || itemsPerPage == 20 || itemsPerPage == 30))
                ? itemsPerPage
                : 10;
        int from = safeItemsPerPage * (currentPage - 1);

        Query matchQuery = MatchQuery.of(
                q -> q
                        .field("content")
                        .query(query))._toQuery();

        SearchResponse<ObjectNode> response;
        try {
            response = elasticsearchClient.search(s -> s
                    .index("wikipedia")
                    .from(from)
                    .size(safeItemsPerPage)
                    .query(matchQuery)
                    .highlight(h -> h
                            .requireFieldMatch(false)
                            .fields("content", f -> f
                                    .preTags("<mark>")
                                    .postTags("</mark>")
                                    .fragmentSize(150)        // define o tamanho de cada trecho (ajustável)
                                    .numberOfFragments(3)     // tenta retornar até 3 trechos
                                    .noMatchSize(0)           // não retorna conteúdo se não houver match
                            )
                    ),ObjectNode.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public List<String> getSuggestions(String query) {
        try {
            SearchResponse<ObjectNode> response = elasticsearchClient.search(s -> s
                            .index("wikipedia_new")  // Usando o novo índice
                            .size(0)
                            .suggest(sug -> sug
                                    .text(query)
                                    .suggesters("suggestions", sugBuilder -> sugBuilder
                                            .term(t -> t
                                                    .field("content_suggest")
                                                    .suggestMode(SuggestMode.Always)
                                                    .minDocFreq(1f)
                                                    .size(5)
                                            )
                                    )
                            ),
                    ObjectNode.class
            );

            return response.suggest().get("suggestions").stream()
                    .flatMap(suggestion -> suggestion.term().options().stream())
                    .map(option -> option.text())
                    .distinct()
                    .limit(5)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException("Erro ao obter sugestões de pesquisa", e);
        }
    }

}
