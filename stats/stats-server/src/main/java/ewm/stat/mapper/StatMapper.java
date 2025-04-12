package ewm.stat.mapper;

import ewm.stat.model.Hit;
import statdto.StatDtoRequest;

import java.time.LocalDateTime;

public class StatMapper {
    public static Hit toHit(StatDtoRequest dtoRequest) {
        Hit hit = new Hit();
        hit.setApp(dtoRequest.getApp());
        hit.setUri(dtoRequest.getUri());
        hit.setIp(dtoRequest.getIp());
        hit.setHitDateTime(LocalDateTime.parse(dtoRequest.getTimestamp()));
        return hit;
    }

}
