package ru.practicum.ewm.comment.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommentRequestDto {
    @NotBlank
    String text;
    @NotNull
    Long event;
}
