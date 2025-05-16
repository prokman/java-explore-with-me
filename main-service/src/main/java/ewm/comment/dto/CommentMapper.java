package ewm.comment.dto;

import ewm.comment.model.Comment;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Clock;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentDto commentToDto(Comment comment);

    @Mapping(target = "created", expression = "java(java.time.LocalDateTime.now(clock))")
    @Mapping(target = "authorId", source = "authorId")
    @Mapping(target = "eventId", source = "eventId")
    Comment newCommentDtoToComment(NewCommentDto request, Long eventId, Long authorId, @Context Clock clock);
}
