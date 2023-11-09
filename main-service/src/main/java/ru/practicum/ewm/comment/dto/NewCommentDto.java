package ru.practicum.ewm.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public final class NewCommentDto {
    @Size(max = 3000)
    @NotBlank
    private final String text;

    @JsonCreator
    public NewCommentDto(@JsonProperty("text") String text) {
        this.text = text;
    }
}
