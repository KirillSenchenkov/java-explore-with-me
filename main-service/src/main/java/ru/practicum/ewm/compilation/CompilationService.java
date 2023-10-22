package ru.practicum.ewm.compilation;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.RequestRepository;
import ru.practicum.ewm.request.dto.ConfirmedRequests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.ewm.request.model.RequestStatus.CONFIRMED;

@Service
@AllArgsConstructor
@Transactional
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.dtoToCompilation(newCompilationDto);
        if (newCompilationDto.getEvents() != null) {
            compilation.setEvents(eventRepository.findAllByIdIn(newCompilationDto.getEvents()));
        }
        CompilationDto compilationDto = CompilationMapper.compilationToDto(compilationRepository.save(compilation));
        if (compilation.getEvents() != null) {
            List<Long> ids = compilation.getEvents().stream().map(Event::getId).collect(Collectors.toList());
            Map<Long, Long> confirmedRequests = requestRepository.findAllByEventIdInAndStatus(ids, CONFIRMED)
                    .stream()
                    .collect(Collectors.toMap(ConfirmedRequests::getEvent, ConfirmedRequests::getCount));
            return new CompilationDto(compilationDto.getId(),compilation.getEvents().stream()
                    .map(event -> EventMapper.eventToEventShortDto(event, confirmedRequests.get(event.getId())))
                    .collect(Collectors.toList()), compilationDto.getPinned(), compilationDto.getTitle());
        } else {
            return compilationDto;
        }
    }

    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilation) {
        Compilation compilation = getCompilation(compId);
        if (updateCompilation.getEvents() != null) {
            Set<Event> events = updateCompilation.getEvents().stream().map(id -> {
                Event event = new Event();
                event.setId(id);
                return event;
            }).collect(Collectors.toSet());
            compilation.setEvents(events);
        }
        if (updateCompilation.getPinned() != null) {
            compilation.setPinned(updateCompilation.getPinned());
        }
        String title = updateCompilation.getTitle();
        if (title != null && !title.isBlank()) {
            compilation.setTitle(title);
        }
        CompilationDto compilationDto = CompilationMapper.compilationToDto(compilationRepository.save(compilation));
        if (compilation.getEvents() != null) {
            List<Long> ids = compilation.getEvents().stream().map(Event::getId).collect(Collectors.toList());
            Map<Long, Long> confirmedRequests = requestRepository.findAllByEventIdInAndStatus(ids, CONFIRMED)
                    .stream()
                    .collect(Collectors.toMap(ConfirmedRequests::getEvent, ConfirmedRequests::getCount));
            return new CompilationDto(compilationDto.getId(),compilation.getEvents().stream()
                    .map(event -> EventMapper.eventToEventShortDto(event, confirmedRequests.get(event.getId())))
                    .collect(Collectors.toList()), compilationDto.getPinned(), compilationDto.getTitle());
        }
        return compilationDto;
    }

    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (pinned != null) {
            List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageable);
            List<CompilationDto> result = new ArrayList<>();
            List<ConfirmedRequests> confirmedRequestsList = requestRepository.findAllByStatus(CONFIRMED);
            for (Compilation compilation : compilations) {
                CompilationDto compilationDto = CompilationMapper.compilationToDto(compilation);
                if (compilation.getEvents() != null) {
                    List<Long> ids = compilation.getEvents().stream().map(Event::getId).collect(Collectors.toList());
                    List<ConfirmedRequests> sortedConfirmedRequests = new ArrayList<>();
                    for (Long id : ids) {
                        for (ConfirmedRequests confirmedRequest : confirmedRequestsList) {
                            if (confirmedRequest.getEvent().equals(id)) {
                                sortedConfirmedRequests.add(confirmedRequest);
                            }
                        }
                    }
                    Map<Long, Long> confirmedRequests = sortedConfirmedRequests
                            .stream()
                            .collect(Collectors.toMap(ConfirmedRequests::getEvent, ConfirmedRequests::getCount));
                    CompilationDto compilationDtoWithEvents = new CompilationDto(compilationDto.getId(),
                            compilation.getEvents().stream()
                            .map(event -> EventMapper.eventToEventShortDto(event, confirmedRequests.get(event.getId())))
                            .collect(Collectors.toList()), compilationDto.getPinned(), compilationDto.getTitle());
                    result.add(compilationDtoWithEvents);
                } else {
                    result.add(compilationDto);
                }
            }
            return result;
        } else {
            List<Compilation> compilations = compilationRepository.findAll(pageable).getContent();
            List<CompilationDto> result = new ArrayList<>();
            List<ConfirmedRequests> confirmedRequestsList = requestRepository.findAllByStatus(CONFIRMED);
            for (Compilation compilation : compilations) {
                CompilationDto compilationDto = CompilationMapper.compilationToDto(compilation);
                if (compilation.getEvents() != null) {
                    List<Long> ids = compilation.getEvents().stream().map(Event::getId).collect(Collectors.toList());
                    List<ConfirmedRequests> sortedConfirmedRequests = new ArrayList<>();
                    for (Long id : ids) {
                        for (ConfirmedRequests confirmedRequest : confirmedRequestsList) {
                            if (confirmedRequest.getEvent().equals(id)) {
                                sortedConfirmedRequests.add(confirmedRequest);
                            }
                        }
                    }
                    Map<Long, Long> confirmedRequests = sortedConfirmedRequests
                            .stream()
                            .collect(Collectors.toMap(ConfirmedRequests::getEvent, ConfirmedRequests::getCount));
                    CompilationDto compilationDtoWithEvents = new CompilationDto(compilationDto.getId(),
                            compilation.getEvents().stream()
                                    .map(event -> EventMapper
                                            .eventToEventShortDto(event, confirmedRequests.get(event.getId())))
                                    .collect(Collectors.toList()), compilationDto.getPinned(), compilationDto.getTitle());
                    result.add(compilationDtoWithEvents);
                } else {
                    result.add(compilationDto);
                }
            }
            return result;
        }
    }

    @Transactional(readOnly = true)
    public CompilationDto getCompilationById(Long compilationId) {
        Compilation compilation = getCompilation(compilationId);
        CompilationDto compilationDto = CompilationMapper.compilationToDto(compilation);
        if (compilation.getEvents() != null) {
            List<Long> ids = compilation.getEvents().stream().map(Event::getId).collect(Collectors.toList());
            Map<Long, Long> confirmedRequests = requestRepository.findAllByEventIdInAndStatus(ids, CONFIRMED)
                    .stream()
                    .collect(Collectors.toMap(ConfirmedRequests::getEvent, ConfirmedRequests::getCount));
            return new CompilationDto(compilationDto.getId(),compilation.getEvents().stream()
                    .map(event -> EventMapper.eventToEventShortDto(event, confirmedRequests.get(event.getId())))
                    .collect(Collectors.toList()), compilationDto.getPinned(), compilationDto.getTitle());
        }
        return compilationDto;
    }

    public void deleteCompilation(Long compilationId) {
        getCompilation(compilationId);
        compilationRepository.deleteById(compilationId);
    }

    private Compilation getCompilation(Long compilationId) {
        return compilationRepository.findById(compilationId).orElseThrow(() ->
                new NotFoundException("Компиляция не найдена"));
    }
}
