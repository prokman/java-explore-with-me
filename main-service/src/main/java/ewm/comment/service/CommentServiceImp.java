package ewm.comment.service;

import ewm.comment.dto.CommentDto;
import ewm.comment.dto.CommentMapper;
import ewm.comment.dto.NewCommentDto;
import ewm.comment.model.Comment;
import ewm.comment.repository.CommentRepository;
import ewm.exceptions.BadRequestException;
import ewm.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImp implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentDto postComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        Clock clock = Clock.systemDefaultZone();
        Comment comment = commentMapper.newCommentDtoToComment(newCommentDto, eventId, userId, clock);
        return commentMapper.commentToDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto patchComment(Long userId, Long comId, NewCommentDto newCommentDto) {
        Comment comment = commentRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException("комментарий с ИД-" + comId + " не найден"));
        if (!comment.getAuthorId().equals(userId)) {
            throw new BadRequestException("пользователь " + userId + " не является автором комментария " + comId);
        }
        comment.setText(newCommentDto.getText());
        Comment updateComment = commentRepository.save(comment);
        return commentMapper.commentToDto(updateComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException("Comment with ID " + commentId + " not found");
        }
        if (!commentRepository.existsByIdAndAuthorId(commentId, userId)) {
            throw new NotFoundException("Автор " + userId + " не является автором комментария " + commentId);
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getComments(Integer from, Integer size, Long eventId) {
        if (size.equals(0)) {
            throw new BadRequestException("параметр size не может быть равен 0");
        }
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);
        List<Comment> commentList = commentRepository.findAllCommentsByEventId(eventId, pageable);
        List<CommentDto> commentDtoList = commentList.stream().map(commentMapper::commentToDto).toList();
        return commentDtoList;
    }
}
