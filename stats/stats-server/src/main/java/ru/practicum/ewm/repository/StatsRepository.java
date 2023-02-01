package ru.practicum.ewm.repository;

import ru.practicum.ewm.dto.ViewStatsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT NEW ru.practicum.ewm.dto.ViewStatsDto(eh.app, eh.uri, COUNT(eh.uri))" +
            " FROM EndpointHit eh" +
            " WHERE eh.uri IN :uris " +
            " AND eh.timestamp BETWEEN :start AND :end " +
            " GROUP BY eh.app, eh.uri " +
            " ORDER BY COUNT (eh.uri) DESC")
    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT NEW ru.practicum.ewm.dto.ViewStatsDto(eh.app, eh.uri, COUNT(DISTINCT eh.uri))" +
            " FROM EndpointHit eh" +
            " WHERE eh.uri IN :uris " +
            " AND eh.timestamp BETWEEN :start AND :end " +
            " GROUP BY eh.app, eh.uri " +
            " ORDER BY COUNT (eh.uri) DESC")
    List<ViewStatsDto> getStatsUnique(LocalDateTime start, LocalDateTime end, List<String> uris);
}