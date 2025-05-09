package ewm.compilation.repository;

import ewm.compilation.model.Compilation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @EntityGraph(attributePaths = {"events.category", "events.initiator"})
    Optional<Compilation> findFullById(Long compId);

    @EntityGraph(attributePaths = {"events.category", "events.initiator"})
    @Query("SELECT comp FROM Compilation comp Where comp.pinned = :pinned ORDER BY comp.id ASC")
    Page<Compilation> getFullCompilations(Pageable pageable, @Param("pinned")Boolean pinned);

    @EntityGraph(attributePaths = {"events.category", "events.initiator"})
    Page<Compilation> findByPinned(Boolean pinned, Pageable pageable);



}
