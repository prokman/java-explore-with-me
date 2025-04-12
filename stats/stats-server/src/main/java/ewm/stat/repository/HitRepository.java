package ewm.stat.repository;

import ewm.stat.model.Hit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import statdto.StatDtoResponse;

import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query(
            value = "SELECT h.APP as app, h.URI as uri, COUNT(DISTINCT IP) AS hits FROM hits h" +
            " WHERE hitDateTime BETWEEN :start AND :end" +
            " AND LOWER(hit.uri) IN (:uris)" +
            " GROUP BY app, uri", nativeQuery = true
    )
    List<StatDtoResponse> getHitListUniqueIp(String start, String end, List<String> uris);

    @Query(
            value = "SELECT h.APP as app, h.URI as uri, COUNT(IP) AS hits FROM hits h" +
                    " WHERE hitDateTime BETWEEN :start AND :end" +
                    " AND LOWER(hit.uri) IN (:uris)" +
                    " GROUP BY app, uri", nativeQuery = true
    )
    List<StatDtoResponse> getHitListTotal(String start, String end, List<String> uris);
}
