package ewm.comment.repository;

import ewm.comment.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllCommentsByEventId(Long eventId, Pageable pageable);

    Boolean existsByIdAndAuthorId(Long commentId, Long userId);
}
