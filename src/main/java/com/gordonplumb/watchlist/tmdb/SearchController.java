package com.gordonplumb.watchlist.tmdb;

import com.gordonplumb.watchlist.tmdb.models.MovieDetailsDTO;
import com.gordonplumb.watchlist.tmdb.models.TmdbSearchResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final TmdbService service;

    public SearchController(TmdbService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<TmdbSearchResult> search(
        @RequestParam String query,
        @RequestParam int pageNumber
    ) {
        return ResponseEntity.ok(this.service.search(query, pageNumber));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDetailsDTO> getDetails(@PathVariable(value="id") int id) {
        return ResponseEntity.ok(this.service.getDetails(id));
    }
}
