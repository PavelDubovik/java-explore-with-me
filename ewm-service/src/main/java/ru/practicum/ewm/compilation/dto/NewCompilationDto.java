package ru.practicum.ewm.compilation.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class NewCompilationDto {
    @NotBlank
    private String title;
    private final Boolean pinned = false;
    private Set<Long> events;
}
