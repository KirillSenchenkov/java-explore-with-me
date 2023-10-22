package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@AllArgsConstructor
public final class UpdateCompilationRequest {
    private final List<Long> events;

    private final Boolean pinned;

    @Size(min = 1, max = 50)
    private final String title;

    /*@JsonCreator
    public UpdateCompilationRequest(@JsonProperty("events") List<Long> events,
                                    @JsonProperty("pinned") Boolean pinned,
                                    @JsonProperty("title") String title) {
        this.events = events;
        this.pinned = pinned;
        this.title = title;
    }*/
}
