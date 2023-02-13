package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventService {
    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getEvents(Long userId, int from, int size);

    EventFullDto getEventById(Long eventId, Long userId);

    EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEvent);

    List<EventFullDto> getEventsByParameters(List<Long> users, EventState[] states, Long[] categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEvent);

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    Set<EventDto> getEvents(Set<Long> events);

    List<EventShortDto> getEventsForUserByParameters(String text, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean paid, Boolean onlyAvailable, String sort, int from, int size);

    EventFullDto getUserEventById(Long id);

    List<ParticipationRequestDto> getRequestsByEventAndUser(Long eventId, Long userId);
}
