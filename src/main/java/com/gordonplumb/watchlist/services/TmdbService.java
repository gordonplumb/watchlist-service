package com.gordonplumb.watchlist.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gordonplumb.watchlist.models.dtos.*;
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
import java.util.Arrays;
import java.util.HashMap;

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
            MovieDetailsDTO details = objectMapper.readValue(body, MovieDetailsDTO.class);
            CreditsDTO credits = details.getCredits();
            CrewDTO[] crew = credits.getCrew();
            HashMap<Integer, CrewDTO> crewMap = new HashMap<>();
            for (int i = 0; i < crew.length && crewMap.size() < 10; i++) {
                CrewDTO crewMember = crew[i];
                crewMap.putIfAbsent(crewMember.getId(), crewMember);
            }
            credits.setCrew(crewMap.values().toArray(CrewDTO[]::new));
            credits.setCast(Arrays.stream(credits.getCast(), 0, 10).toArray(CastDTO[]::new));
            return details;
        } catch (Exception ex) {
            throw new InternalServerException("Unexpected error mapping API response");
        }
    }

    private String sendRequest(URI uri) {
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
                return response.body();
            } catch (Exception ex) {
                throw new InternalServerException("Unexpected error mapping API response");
            }
        }

        throw new InternalServerException(String.valueOf(response.statusCode()));
    }
}
