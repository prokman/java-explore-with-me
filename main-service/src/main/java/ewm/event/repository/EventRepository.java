package ewm.event.repository;

import ewm.event.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    @EntityGraph(attributePaths = {"category", "initiator", "location"})
    Optional<Event> findWithDetailsById(Long eventId);

    @EntityGraph(attributePaths = {"category", "initiator", "location"})
    @Query("SELECT ev FROM Event ev ORDER BY ev.id")
    List<Event> getFullEventsByParam(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"category", "initiator", "location"})
    Page<Event> findAll(Specification<Event> specification, Pageable pageable);

    @EntityGraph(attributePaths = {"category", "initiator"})
    Set<Event> findByIdIn(Set<Long> eventIds);

    @Query("SELECT e.id FROM Event e WHERE e.category.id = :catId")
    List<Long> findIdsByCategoryId(@Param("catId") Long catId);

}
