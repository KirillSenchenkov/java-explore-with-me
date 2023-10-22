package ru.practicum.ewm.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.request.RequestRepository;
import ru.practicum.ewm.request.dto.ConfirmedRequests;
import ru.practicum.ewm.user.UserMapper;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.ewm.event.model.State.PUBLISHED;
import static ru.practicum.ewm.request.model.RequestStatus.CONFIRMED;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    public CommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User author = checkAndGetUser(userId);
        Event event = checkAndGetEvent(eventId);
        if (event.getState() != PUBLISHED) {
            throw new ValidationException("Комментировать можно только опубликованные события");
        }
        Comment comment = commentRepository.save(CommentMapper.dtoToComment(newCommentDto, author, event));
        UserShortDto userShort = UserMapper.userToUserShortDto(author);
        EventShortDto eventShort = EventMapper.eventToEventShortDto(event,
                requestRepository.countByEventIdAndStatus(eventId, CONFIRMED));
        return CommentMapper.commentToDto(comment, userShort, eventShort);
    }

    public CommentDto updateComment(Long userId, Long eventId, Long commentId, NewCommentDto newCommentDto) {
        User author = checkAndGetUser(userId);
        Event event = checkAndGetEvent(eventId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Комментарий не найден"));
        if (comment.getEvent() != event) {
            throw new ValidationException("Комментарий относится к другому событию");
        }
        comment.setText(newCommentDto.getText());
        comment.setEdited(LocalDateTime.now());
        UserShortDto userShort = UserMapper.userToUserShortDto(author);
        EventShortDto eventShort = EventMapper.eventToEventShortDto(event,
                requestRepository.countByEventIdAndStatus(eventId, CONFIRMED));
        return CommentMapper.commentToDto(comment, userShort, eventShort);
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByAuthor(Long userId, Integer from, Integer size) {
        User author = checkAndGetUser(userId);
        List<Comment> comments = commentRepository.findAllByAuthorId(userId, PageRequest.of(from / size, size));
        List<Long> eventIds = comments.stream().map(comment -> comment.getEvent().getId()).collect(Collectors.toList());
        Map<Long, Long> confirmedRequests = requestRepository.findAllByEventIdInAndStatus(eventIds, CONFIRMED)
                .stream()
                .collect(Collectors.toMap(ConfirmedRequests::getEvent, ConfirmedRequests::getCount));
        UserShortDto userShort = UserMapper.userToUserShortDto(author);
        List<CommentDto> result = new ArrayList<>();
        for (Comment c : comments) {
            Long eventId  = c.getEvent().getId();
            EventShortDto eventShort = EventMapper.eventToEventShortDto(c.getEvent(), confirmedRequests.get(eventId));
            result.add(CommentMapper.commentToDto(c, userShort, eventShort));
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getComments(Long eventId, Integer from, Integer size) {
        Event event = checkAndGetEvent(eventId);
        EventShortDto eventShort = EventMapper.eventToEventShortDto(event,
                requestRepository.countByEventIdAndStatus(eventId, CONFIRMED));
        return commentRepository.findAllByEventId(eventId, PageRequest.of(from / size, size))
                .stream()
                .map(c -> CommentMapper.commentToDto(c, UserMapper.userToUserShortDto(c.getAuthor()), eventShort))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CommentDto getCommentById(Long commentId) {
        Comment comment = checkAndGetComment(commentId);
        UserShortDto userShort = UserMapper.userToUserShortDto(comment.getAuthor());
        EventShortDto eventShort = EventMapper.eventToEventShortDto(comment.getEvent(),
                requestRepository.countByEventIdAndStatus(comment.getEvent().getId(), CONFIRMED));
        return CommentMapper.commentToDto(comment, userShort, eventShort);
    }

    public void deleteComment(Long userId, Long commentId) {
        User author = checkAndGetUser(userId);
        Comment comment = checkAndGetComment(commentId);
        if (comment.getAuthor() != author) {
            throw new ValidationException("Только автор комментария может его удалить");
        }
        commentRepository.deleteById(commentId);
    }

    public void deleteComment(Long commentId) {
        checkAndGetComment(commentId);
        commentRepository.deleteById(commentId);
    }

    private User checkAndGetUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
    }

    private Event checkAndGetEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие не найдено"));
    }

    private Comment checkAndGetComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Комментарий не найден"));
    }
}
