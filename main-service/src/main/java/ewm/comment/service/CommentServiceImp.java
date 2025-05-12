package ewm.comment.service;

import ewm.comment.dto.CommentDto;
import ewm.comment.dto.CommentMapper;
import ewm.comment.dto.NewCommentDto;
import ewm.comment.model.Comment;
import ewm.comment.repository.CommentRepository;
import ewm.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImp implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public CommentDto postComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        Comment comment = CommentMapper.newCommentDtoToComment(newCommentDto, eventId, userId);
        return CommentMapper.commentToDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto patchComment(Long userId, Long comId, NewCommentDto newCommentDto) {
        Comment comment = commentRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException("комментарий с ИД-" + comId + " не найден"));
        comment.setText(newCommentDto.getText());
        Comment updateComment = commentRepository.save(comment);
        return CommentMapper.commentToDto(updateComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException("Comment with ID " + commentId + " not found");
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getComments(Integer from, Integer size, Long eventId) {
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);
        List<Comment> commentList = commentRepository.findAllCommentsByEventId(eventId, pageable);
        if (commentList.isEmpty()) {
            throw new NotFoundException("Комментарии не найдены");
        }
        List<CommentDto> commentDtoList = commentList.stream().map(CommentMapper::commentToDto).toList();
        return commentDtoList;
    }
}
