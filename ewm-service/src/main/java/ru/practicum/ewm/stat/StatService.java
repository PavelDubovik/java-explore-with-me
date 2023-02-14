package ru.practicum.ewm.stat;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    void createHit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getStats(LocalDateTime startDate, List<Long> eventIds) throws JsonProcessingException;
}
