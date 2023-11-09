package ru.practicum.ewm.compilation.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.List;

@Getter
public final class CompilationDto {
    private final Long id;
    private List<EventShortDto> events;
    private final Boolean pinned;
    private final String title;

    public CompilationDto(Long id, boolean pinned, String title) {
        this.id = id;
        this.pinned = pinned;
        this.title = title;
    }

    @JsonCreator
    public CompilationDto(@JsonProperty("id") Long id,
                          @JsonProperty("events") List<EventShortDto> events,
                          @JsonProperty("pinned") Boolean pinned,
                          @JsonProperty("title") String title) {
        this.id = id;
        this.events = events;
        this.pinned = pinned;
        this.title = title;
    }
}
