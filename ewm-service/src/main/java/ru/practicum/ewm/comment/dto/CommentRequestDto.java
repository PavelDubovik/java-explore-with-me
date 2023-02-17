package ru.practicum.ewm.comment.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@Builder
@Jacksonized
public class CommentRequestDto {
    @NotBlank
    String text;
    @NotNull
    Long event;
}
