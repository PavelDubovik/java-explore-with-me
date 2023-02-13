package ru.practicum.ewm.category.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@Jacksonized
public class NewCategoryDto {
    @NotBlank
    private String name;
}
