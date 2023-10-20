package ru.practicum.ewm.request;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.exception.IncorrectRequestException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.request.model.RequestStatus.*;

@Service
@AllArgsConstructor
@Transactional
public class RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        User user = getUser(userId);
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new IncorrectRequestException("Запрос уже существует");
        }
        if (userId.equals(event.getInitiator().getId())) {
            throw new IncorrectRequestException("Создатель не может сделать запрос на собственное событие");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new IncorrectRequestException("Участие возможно только в опубликованных событиях");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <=
                requestRepository.countByEventIdAndStatus(eventId, CONFIRMED)) {
            throw new IncorrectRequestException("Достигнут лимит участников");
        }
        ParticipationRequest request = new ParticipationRequest();
        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        request.setRequester(user);

        if (event.getRequestModeration() && event.getParticipantLimit() != 0) {
            request.setStatus(PENDING);
        } else {
            request.setStatus(CONFIRMED);
        }
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    public EventRequestStatusUpdateResult updateRequestsStatus(Long userId, Long eventId,
                                                               EventRequestStatusUpdateRequest statusUpdateRequest) {
        User initiator = getUser(userId);
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() ->
                new NotFoundException("Событие не найдено"));
        if (!event.getInitiator().equals(initiator)) {
            throw new ValidationException("Пользователь не является создателем события");
        }
        long confirmedRequests = requestRepository.countByEventIdAndStatus(eventId, CONFIRMED);
        if (event.getParticipantLimit() > 0 && event.getParticipantLimit() <= confirmedRequests) {
            throw new IncorrectRequestException("Достигнут лимит участников");
        }
        List<ParticipationRequestDto> confirmed = new ArrayList<>();
        List<ParticipationRequestDto> rejected = new ArrayList<>();
        List<ParticipationRequest> requests = requestRepository.findAllByEventIdAndIdInAndStatus(eventId,
                statusUpdateRequest.getRequestIds(), PENDING);
        for (int i = 0; i < requests.size(); i++) {
            ParticipationRequest request = requests.get(i);
            if (statusUpdateRequest.getStatus() == REJECTED) {
                request.setStatus(REJECTED);
                rejected.add(RequestMapper.toParticipationRequestDto(request));
            }
            if (statusUpdateRequest.getStatus() == CONFIRMED && event.getParticipantLimit() > 0
                    && (confirmedRequests + i) < event.getParticipantLimit()) {
                request.setStatus(CONFIRMED);
                confirmed.add(RequestMapper.toParticipationRequestDto(request));
            } else {
                request.setStatus(REJECTED);
                rejected.add(RequestMapper.toParticipationRequestDto(request));
            }
        }
        return new EventRequestStatusUpdateResult(confirmed, rejected);
    }

    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findByIdAndRequesterId(requestId, userId);
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestsByEventOwner(Long userId, Long eventId) {
        checkUser(userId);
        eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() ->
                new NotFoundException("Событие не найдено"));
        return requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestsByUser(Long userId) {
        checkUser(userId);
        return requestRepository.findAllByRequesterId(userId).stream()
                .map(RequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
    }

    private void checkUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
    }
}
