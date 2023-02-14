package ru.practicum.ewm.category.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@RequiredArgsConstructor
public class NewCategoryDto {
    @NotBlank
    private String name;
}
