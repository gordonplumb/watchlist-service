package com.gordonplumb.watchlist.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gordonplumb.watchlist.models.dtos.TmdbSearchResult;
import com.gordonplumb.watchlist.models.exceptions.BadRequestException;
import com.gordonplumb.watchlist.models.exceptions.InternalServerException;
import com.gordonplumb.watchlist.models.exceptions.TooManyRequestsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class TmdbService {

    @Value("${tmdb.token}")
    private String token;
    private final String host = "api.themoviedb.org";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final URI baseUri = UriComponentsBuilder.newInstance()
        .scheme("https")
        .host(host)
        .path("/3/")
        .build()
        .toUri();

    public TmdbService() {}

    public TmdbSearchResult search(String query, int pageNumber) {
        String searchString = query.strip();
        if (searchString.isEmpty()) {
            throw new BadRequestException("Search query is empty");
        }
        if (pageNumber < 1) {
            throw new BadRequestException("Page number should be at least 1");
        }

        URI uri = UriComponentsBuilder
            .fromUri(baseUri)
            .pathSegment("search/movie")
            .queryParam("query", searchString)
            .queryParam("page", pageNumber)
            .build()
            .toUri();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .header("Authorization", "Bearer " + token)
            .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new InternalServerException("Unexpected error sending TMDB API request");
        }

        if (response.statusCode() == 429) {
            // TODO introduce some delay
            throw new TooManyRequestsException("Too many requests to TMDB API");
        }

        if (response.statusCode() == 200) {
            try {
                return objectMapper.readValue(response.body(), TmdbSearchResult.class);
            } catch (Exception ex) {
                throw new InternalServerException("Unexpected error mapping API response");
            }
        }

        throw new InternalServerException(String.valueOf(response.statusCode()));
    }
}
