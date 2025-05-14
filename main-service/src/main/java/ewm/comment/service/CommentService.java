package ewm.comment.service;

import ewm.comment.dto.CommentDto;
import ewm.comment.dto.NewCommentDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public interface CommentService {
    CommentDto postComment(@NotNull @Positive Long userId, @Positive Long eventId, NewCommentDto newCommentDto);

    CommentDto patchComment(@NotNull @Positive Long userId, @Positive Long comId, NewCommentDto newCommentDto);

    void deleteComment(@Positive @NotNull Long commentId, @NotNull @Positive Long userId);

    List<CommentDto> getComments(Integer from, Integer size, @NotNull @Positive Long eventId);
}
