package ru.practicum.ewm.category.dto;

import lombok.Data;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class CategoryDto {
    private Long id;
    private String name;
}
