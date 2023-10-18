package ru.practicum.ewm.stats.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;


@Getter
public final class EndpointHitDto {

    @NotBlank
    private final String app;

    @NotBlank
    private final String uri;

    @NotBlank
    private final String ip;

    @NotNull
    @Past
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;

    @JsonCreator
    public EndpointHitDto(@JsonProperty("app") String app,
                          @JsonProperty("uri") String uri,
                          @JsonProperty("ip") String ip,
                          @JsonProperty("timestamp") LocalDateTime timestamp) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
    }
}