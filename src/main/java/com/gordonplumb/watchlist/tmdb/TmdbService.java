package com.gordonplumb.watchlist.tmdb;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import com.gordonplumb.watchlist.exceptions.*;
import com.gordonplumb.watchlist.tmdb.models.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

@Service
public class TmdbService {

    @Value("${tmdb.token}")
    private String token;
    private final String host = "api.themoviedb.org";
    private final HttpClient client = HttpClient.newHttpClient();

    private final RateLimiter rateLimiter = RateLimiter.create(50);
    private final ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final URI baseUri = UriComponentsBuilder.newInstance()
        .scheme("https")
        .host(host)
        .path("/3/")
        .build()
        .toUri();

    private long rateLimitMs = 0;

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

        String body = sendRequest(uri);
        try {
            return objectMapper.readValue(body, TmdbSearchResult.class);
        } catch (Exception ex) {
            throw new InternalServerException("Unexpected error mapping API response");
        }
    }

    public MovieDetailsDTO getDetails(int id) {
        URI uri = UriComponentsBuilder
            .fromUri(baseUri)
            .pathSegment(String.format("movie/%s", id))
            .queryParam("append_to_response", "credits")
            .build()
            .toUri();

        String body = sendRequest(uri);
        try {
            return objectMapper.readValue(body, MovieDetailsDTO.class);
        } catch (Exception ex) {
            throw new InternalServerException("Unexpected error mapping API response");
        }
    }

    private String sendRequest(URI uri) {
        String tooManyRequestsMessage = "Too many requests to TMDB API";
        if (
            (rateLimitMs != 0 && System.currentTimeMillis() < rateLimitMs) ||
            !rateLimiter.tryAcquire(1, 500, TimeUnit.MILLISECONDS)
        ) {
            throw new TooManyRequestsException(tooManyRequestsMessage);
        } else if (rateLimitMs != 0) {
            rateLimitMs = 0;
        }

        HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .header("Authorization", "Bearer " + token)
            .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new InternalServerException(tooManyRequestsMessage);
        }

        if (response.statusCode() == 429) {
            rateLimitMs = System.currentTimeMillis() + 1000;
            throw new TooManyRequestsException("Too many requests to TMDB API");
        }

        if (response.statusCode() == 200) {
            try {
                return response.body();
            } catch (Exception ex) {
                throw new InternalServerException("Unexpected error mapping API response");
            }
        }

        throw new InternalServerException(String.valueOf(response.statusCode()));
    }
}
