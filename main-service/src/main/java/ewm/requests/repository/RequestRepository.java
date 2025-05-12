package ewm.requests.repository;

import ewm.requests.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByRequesterId(Long requesterId);

    List<Request> findAllByEventId(Long eventId);

    Boolean existsByRequesterIdAndEventId(Long requesterId, Long eventId);
}
