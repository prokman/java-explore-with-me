package ewm.categories.repository;

import ewm.categories.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndId(String name, Long id);

    @Query("SELECT cat FROM Category cat ORDER BY cat.id")
    List<Category> getCatByParam(Pageable pageable);

}
