package ewm.stat.service;

import ewm.stat.mapper.StatMapper;
import ewm.stat.model.Hit;
import ewm.stat.repository.HitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import statdto.StatDtoRequest;
import statdto.StatDtoResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatServiceImpl implements StatService {
    private final HitRepository hitRepository;

    @Override
    @Transactional
    public void addHit(StatDtoRequest dtoRequest) {
        Hit hit = StatMapper.toHit(dtoRequest);
        hitRepository.save(hit);
    }

    @Override
    public List<StatDtoResponse> getStats(String start, String end, List<String> uris, Boolean uniqueIp) {
        List<StatDtoResponse> dtoResponses;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startLdt = LocalDateTime.parse(start, formatter);
        LocalDateTime endLdt = LocalDateTime.parse(end, formatter);

        if (uniqueIp) {
            dtoResponses = hitRepository.getHitListUniqueIp(startLdt, endLdt, uris);
            return dtoResponses;
        } else {
            dtoResponses = hitRepository.getHitListTotal(startLdt, endLdt, uris);
            return dtoResponses;
        }
    }
}
