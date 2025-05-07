package stat;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;
import statdto.StatDtoRequest;
import statdto.StatDtoResponse;

import java.util.List;
import java.util.Optional;

@Service
public class StatsClient {
    private static final String BASE_URL = "http://stats-server:9090";
//    private static final String BASE_URL = "http://localhost:9090";
    private static final String API_PREFIX_HIT = "/hit";
    private static final String API_PREFIX_STATS = "/stats";
    private final RestClient restClient;


    public StatsClient() {
        this.restClient = RestClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }

    public void addHit(StatDtoRequest dtoRequest) {
        restClient.post()
                .uri(API_PREFIX_HIT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(dtoRequest)
                .retrieve()
                .toBodilessEntity();
    }

    public List<StatDtoResponse> getStats(String start, String end,
                                          Optional<List<String>> uris, Optional<Boolean> unique) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end dates must not be null");
        }
        return restClient.get()
                .uri(uriBuilder -> {
                    UriBuilder builder = uriBuilder.path(API_PREFIX_STATS)
                            .queryParam("start", start)
                            .queryParam("end", end);
                    uris.ifPresent(list -> builder.queryParam("uris", String.join(",", list)));
                    unique.ifPresent(val -> builder.queryParam("unique", val));
                    return builder.build();
                })
                .retrieve()
                .body(new ParameterizedTypeReference<List<StatDtoResponse>>() {});
    }
}