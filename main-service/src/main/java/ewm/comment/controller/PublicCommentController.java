package ewm.comment.controller;

import ewm.comment.dto.CommentDto;
import ewm.comment.service.CommentService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/comments")
@RequiredArgsConstructor
public class PublicCommentController {
    private final CommentService commentService;

    @GetMapping(path = "/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getComments(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam(required = false, defaultValue = "3") @Positive Integer size,
                                        @PathVariable @NotNull @Positive Long eventId) {
        return commentService.getComments(from, size, eventId);
    }

}
