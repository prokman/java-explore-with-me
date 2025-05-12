package ewm.comment.dto;

import ewm.comment.model.Comment;

import java.time.LocalDateTime;

public class CommentMapper {
    public static CommentDto commentToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthor(comment.getAuthorId());
        commentDto.setEvent(comment.getEventId());
        commentDto.setCreated(comment.getCreated());
        return commentDto;
    }

    public static Comment newCommentDtoToComment(NewCommentDto request, Long eventId, Long authorId) {
        Comment comment = new Comment();
        comment.setText(request.getText());
        comment.setCreated(LocalDateTime.now());
        comment.setAuthorId(authorId);
        comment.setEventId(eventId);
        return comment;
    }
}
