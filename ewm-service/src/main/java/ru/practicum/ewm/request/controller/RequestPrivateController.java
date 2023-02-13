package ru.practicum.ewm.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.service.RequestService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestPrivateController {

    private final RequestService requestService;
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getRequestsByUser(@PathVariable Long userId) {
        log.info("Getting requests of user with id = {}", userId);
        List<ParticipationRequestDto> requestDtos = requestService.getByUserId(userId);
        return ResponseEntity.status(200).body(requestDtos);
    }

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> createRequest(
            @PathVariable Long userId,
            @RequestParam Long eventId) {
        log.info("Create user id {} request for event id {}", userId, eventId);
        return ResponseEntity.status(201).body(eventService.createRequest(userId, eventId));
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequest(
            @PathVariable Long userId,
            @PathVariable Long requestId) {
        return ResponseEntity.status(200).body(requestService.cancelRequest(userId, requestId));
    }
}
