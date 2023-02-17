package ru.practicum.ewm.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.ewm.comment.model.CommentStatus;

import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class CommentDto {
    Long id;
    String text;
    Long event;
    Long author;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime created;
    CommentStatus status;
}
