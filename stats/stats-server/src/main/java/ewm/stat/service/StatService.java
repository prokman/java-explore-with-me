package ewm.stat.service;

import statdto.StatDtoRequest;
import statdto.StatDtoResponse;

import java.util.List;

public interface StatService {
    void addHit(StatDtoRequest dtoRequest);

    List<StatDtoResponse> getStats(String start, String end, List<String> uris, Boolean unique);
}
