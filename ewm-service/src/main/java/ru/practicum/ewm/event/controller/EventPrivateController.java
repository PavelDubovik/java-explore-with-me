package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class EventPrivateController {

    private final EventService eventService;
    private final RequestService requestService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEventsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Getting list of events: userId = {}, from = {}, size = {}", userId, from, size);
        return ResponseEntity.status(200).body(eventService.getEvents(userId, from, size));
    }

    @PostMapping
    public ResponseEntity<EventFullDto> createEvent(
            @PathVariable Long userId,
            @RequestBody @Valid NewEventDto newEventDto
    ) {
        log.info("Creating new event {}", newEventDto.getTitle());
        return ResponseEntity.status(201).body(eventService.createEvent(userId, newEventDto));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventById(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        log.info("Getting events user id = {}, event id = {}", userId, eventId);
        return ResponseEntity.status(200).body(eventService.getEventById(eventId, userId));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody UpdateEventUserRequest updateEvent) {
        log.info("Updating event with id = {}", eventId);
        return ResponseEntity.status(200).body(eventService.updateEventByUser(userId, eventId, updateEvent));
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getParticipationRequests(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        log.info("Getting participation requests for event id = {} of user id = {}", eventId, userId);
        List<ParticipationRequestDto> requests = eventService.getRequestsByEventAndUser(eventId, userId);
        return ResponseEntity.status(200).body(requests);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateParticipationRequests(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody EventRequestStatusUpdateRequest updateRequest) {
        log.info("Updating participation requests for event id = {} of user id = {}", eventId, userId);
        return ResponseEntity.status(200).body(requestService.updateRequests(userId, eventId, updateRequest));
    }
}
