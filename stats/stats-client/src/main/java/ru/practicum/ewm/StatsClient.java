package ru.practicum.ewm;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.ViewStatsDto;

import java.util.List;

public interface StatsClient {
    void createHit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getStats(String start, String end, List<String> uris, boolean unique) throws JsonProcessingException;
}
