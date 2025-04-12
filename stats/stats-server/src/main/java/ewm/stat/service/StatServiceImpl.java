package ewm.stat.service;

import ewm.stat.mapper.StatMapper;
import ewm.stat.model.Hit;
import ewm.stat.repository.HitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import statdto.StatDtoRequest;
import statdto.StatDtoResponse;

import java.util.ArrayList;
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
        List<StatDtoResponse> dtoResponseList = new ArrayList<>();
        uris.forEach(String::toLowerCase);
        if (uniqueIp) {
            dtoResponseList = hitRepository.getHitListUniqueIp(start, end, uris);
            return dtoResponseList;
        } else {
            dtoResponseList = hitRepository.getHitListTotal(start, end, uris);
            return dtoResponseList;
        }
    }
}
