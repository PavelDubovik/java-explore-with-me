package ru.practicum.ewm.event.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stat.StatService;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("events")
@RequiredArgsConstructor
public class EventPublicController {

    private static final String APPLICATION_NAME = "ewm-service";
    private final EventService eventService;
    private final StatService statService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEventsByFilter(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Getting events by user by parameters");
        return ResponseEntity.status(200)
                .body(eventService.getEventsForUserByParameters(
                        text, categories, rangeStart, rangeEnd, paid, onlyAvailable, sort, from, size));
    }

    @GetMapping("{id}")
    public ResponseEntity<EventFullDto> getEventById(
            @PathVariable Long id,
            HttpServletRequest request) throws JsonProcessingException {
        log.info("Getting events by user by id");
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app(APPLICATION_NAME)
                .timestamp(LocalDateTime.now())
                .build();
        statService.CreateHit(endpointHitDto);
        return ResponseEntity.status(200).body(eventService.getUserEventById(id));
    }
}
