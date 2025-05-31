package com.elasticsearch.search.controller;

import com.elasticsearch.search.api.facade.SearchApi;
import com.elasticsearch.search.api.model.Results;
import com.elasticsearch.search.dto.SearchRequest;
import com.elasticsearch.search.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<Results>> search(@RequestBody SearchRequest request) {
        var result = searchService.search(
                request.getQ(),
                request.getP(),
                request.getCl(),
                request.getItemsPerPage()
        );
        return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(result));
    }
}
