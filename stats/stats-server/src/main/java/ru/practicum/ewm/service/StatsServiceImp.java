package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.mapper.EndpointHitMapper;
import ru.practicum.ewm.dto.ViewStatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.model.EndpointHit;
import ru.practicum.ewm.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImp implements StatsService {

    private final StatsRepository statsRepository;

    private final EndpointHitMapper endpointHitMapper;

    @Override
    public void saveEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = endpointHitMapper.toEndpointHit(endpointHitDto);
        statsRepository.save(endpointHit);
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (unique) {
            return statsRepository.getStatsUnique(start, end, uris);
        } else {
            return statsRepository.getStats(start, end, uris);
        }

    }
}
