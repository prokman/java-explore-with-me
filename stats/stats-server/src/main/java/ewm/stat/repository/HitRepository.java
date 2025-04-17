package ewm.stat.repository;

import ewm.stat.model.Hit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import statdto.StatDtoResponse;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query(
            "SELECT new statdto.StatDtoResponse(h.app, h.uri, COUNT(DISTINCT h.ip) AS hits)" +
                    " FROM Hit h" +
                    " WHERE h.hitDateTime BETWEEN :start AND :end" +
                    " AND (LOWER(h.uri) IN (:uris) OR :uris IS NULL)" +
                    " GROUP BY h.app, h.uri" +
                    " ORDER BY hits DESC"
    )
    List<StatDtoResponse> getHitListUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(
            "SELECT new statdto.StatDtoResponse(h.app, h.uri, COUNT(h.ip) AS hits)" +
                    " FROM Hit h" +
                    " WHERE h.hitDateTime BETWEEN :start AND :end" +
                    " AND (LOWER(h.uri) IN (:uris) OR :uris IS NULL)" +
                    " GROUP BY h.app, h.uri" +
                    " ORDER BY hits DESC"
    )
    List<StatDtoResponse> getHitListTotal(LocalDateTime start, LocalDateTime end, List<String> uris);
}
