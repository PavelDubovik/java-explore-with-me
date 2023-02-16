package ru.practicum.ewm.stat;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.StatsClient;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatServiceImp implements StatService {

    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final StatsClient statsClient;

    @Override
    public void createHit(EndpointHitDto endpointHitDto) {
        statsClient.createHit(endpointHitDto);
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime startDate, List<Long> eventIds) throws JsonProcessingException {
        String end = LocalDateTime.now().format(DateTimeFormatter.ofPattern(FORMAT));
        String start = startDate.format(DateTimeFormatter.ofPattern(FORMAT));
        List<String> uris = eventIds.stream().map(id -> "/events/" + id).collect(Collectors.toList());
        return statsClient.getStats(start, end, uris, true);
    }


}
