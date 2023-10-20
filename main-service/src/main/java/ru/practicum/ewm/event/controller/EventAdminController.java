package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventFullDtoWithViews;
import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class EventAdminController {
    private final EventService eventService;

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId,
                                           @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        return eventService.updateEventByAdmin(eventId, updateEventAdminRequest);
    }

    @GetMapping
    public List<EventFullDtoWithViews> getEventsByAdminParams(@RequestParam(required = false) List<Long> users,
                                                              @RequestParam(required = false) List<String> states,
                                                              @RequestParam(required = false) List<Long> categories,
                                                              @RequestParam(required = false) @DateTimeFormat(pattern =
                                                                      "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                              @RequestParam(required = false) @DateTimeFormat(pattern =
                                                                      "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                              @RequestParam(value = "from", defaultValue = "0")
                                                              @PositiveOrZero Integer from,
                                                              @RequestParam(value = "size", defaultValue = "10")
                                                              @Positive Integer size) {
        return eventService.getEventsByAdminParams(users, states, categories, rangeStart, rangeEnd, from, size);
    }
}
