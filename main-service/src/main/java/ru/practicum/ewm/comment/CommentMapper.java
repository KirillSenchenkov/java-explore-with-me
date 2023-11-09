package ru.practicum.ewm.comment;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public Comment dtoToComment(NewCommentDto newCommentDto, User author, Event event) {
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setEvent(event);
        comment.setText(newCommentDto.getText());
        comment.setCreated(LocalDateTime.now());
        return comment;
    }

    public CommentDto commentToDto(Comment comment, UserShortDto author, EventShortDto event) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                author,
                event,
                comment.getCreated(),
                comment.getEdited()
        );
    }
}
