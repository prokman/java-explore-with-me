package ewm.stat.mapper;

import ewm.stat.model.Hit;
import statdto.StatDtoRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatMapper {
    public static Hit toHit(StatDtoRequest dtoRequest) {
        Hit hit = new Hit();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        hit.setApp(dtoRequest.getApp());
        hit.setUri(dtoRequest.getUri());
        hit.setIp(dtoRequest.getIp());
        hit.setHitDateTime(LocalDateTime.parse(dtoRequest.getTimestamp(), formatter));
        return hit;
    }
}
