package ru.practicum.ewm.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class ViewStatsDto {
    private String app;
    private String uri;
    private Long hits;
}