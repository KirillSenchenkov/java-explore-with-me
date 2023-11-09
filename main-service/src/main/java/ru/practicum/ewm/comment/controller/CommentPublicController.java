package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.comment.CommentService;
import ru.practicum.ewm.comment.dto.CommentDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentPublicController {

    private final CommentService commentService;

    @GetMapping("/event/{eventId}")
    List<CommentDto> getComments(@PathVariable Long eventId,
                                 @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                 @RequestParam(defaultValue = "10") @Positive Integer size) {
        return commentService.getComments(eventId, from, size);
    }

    @GetMapping("/{commentId}")
    CommentDto getCommentById(@PathVariable Long commentId) {
        return commentService.getCommentById(commentId);
    }
}
