package stat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import statdto.StatDtoRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsClient {
    private static final String API_PREFIX_Hit = "/hit";
    private static final String API_PREFIX_Stats = "/stats";
    RestClient restClient = RestClient.create();


    public void addHit(StatDtoRequest dtoRequest) {

    }

    public ResponseEntity<Object> getStats(String start, String end, List<String> uris, Boolean unique) {
        return null;
    }
}
