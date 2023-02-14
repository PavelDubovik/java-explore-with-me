package ru.practicum.ewm.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewStatsDto {
    private String app;
    private String uri;
    private Long hits;
}