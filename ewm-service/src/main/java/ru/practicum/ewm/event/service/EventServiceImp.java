package ru.practicum.ewm.event.service;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.QEvent;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.util.QPredicates;
import ru.practicum.ewm.handler.EventStateException;
import ru.practicum.ewm.handler.RequestException;
import ru.practicum.ewm.handler.ValidateDateTimeException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.dto.RequestState;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImp implements EventService {

    private final EventMapper eventMapper;
    private final RequestMapper requestMapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        if (newEventDto.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new ValidateDateTimeException("Date&Time of Event should be more than 2 hours before now");
        }
        Event event = eventMapper.toEvent(newEventDto);
        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(userRepository.findById(userId).orElseThrow());
        event.setCategory(categoryRepository.findById(newEventDto.getCategory()).orElseThrow());
        event.setState(EventState.PENDING);
        EventFullDto savedEvent = eventMapper.toEventFullDto(eventRepository.save(event));
        log.info("Event with id = {} and title: \"{}\" created", savedEvent.getId(), savedEvent.getTitle());
        return savedEvent;
    }

    @Override
    public List<EventShortDto> getEvents(Long userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return eventMapper.toEventShortDto(eventRepository.findAllByInitiatorId(userId, pageable));
    }

    @Override
    public EventFullDto getEventById(Long eventId, Long userId) {
        Event event = Optional.ofNullable(eventRepository.findByIdAndInitiatorId(eventId, userId)).orElseThrow();
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEvent) {
        Event event = Optional.ofNullable(eventRepository.findByIdAndInitiatorId(eventId, userId)).orElseThrow();
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new RequestException("Published event cannot be update");
        }
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEvent.getCategory()).orElseThrow());
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            if (updateEvent.getEventDate().minusHours(2).isAfter(LocalDateTime.now())) {
                event.setEventDate(updateEvent.getEventDate());
            } else {
                throw new ValidateDateTimeException("eventDate should be no earlier now + 2 hours");
            }
        } else if (event.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new ValidateDateTimeException("eventDate should be no earlier now + 2 hours");
        }
        if (updateEvent.getLocation() != null) {
            event.setLocation(updateEvent.getLocation());
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (!updateEvent.getStateAction().isEmpty()) {
            switch (updateEvent.getStateAction()) {
                case "SEND_TO_REVIEW":
                    event.setState(EventState.PENDING);
                    break;
                case "CANCEL_REVIEW":
                    event.setState(EventState.CANCELED);
                    break;
            }

        }
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> getEventsByParameters(
            List<Long> usersId, EventState[] states, Long[] categories, LocalDateTime rangeStart, LocalDateTime rangeEnd,
            int from, int size) {
        List<Event> events;
        QEvent eventDto = QEvent.event;
        Predicate predicate = QPredicates.builder()
                .add(usersId, eventDto.initiator.id::in)
                .add(states, eventDto.state::in)
                .add(categories, eventDto.category.id::in)
                .add(rangeStart, eventDto.eventDate::after)
                .add(rangeEnd, eventDto.eventDate::before)
                .buildAnd();

        if (predicate == null) {
            events = eventRepository.findAll(PageRequest.of(from / size, size)).toList();
        } else {
            events = eventRepository.findAll(predicate, PageRequest.of(from / size, size)).toList();
        }
        log.info("Events by parameter got");
        return eventMapper.toEventFullDto(events);
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEvent) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEvent.getCategory()).orElseThrow());
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            if (updateEvent.getEventDate().minusHours(1).isAfter(LocalDateTime.now())) {
                event.setEventDate(updateEvent.getEventDate());
            } else {
                throw new ValidateDateTimeException("eventDate should be no earlier now + 2 hours");
            }
        }
        if (updateEvent.getLocation() != null) {
            event.setLocation(updateEvent.getLocation());
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
            if (event.getState().equals(EventState.PENDING)) {
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else {
                throw new EventStateException("Event can be published only if it has State PENDING");
            }
        } else if (updateEvent.getStateAction().equals(StateAction.REJECT_EVENT)) {
            if (!event.getState().equals(EventState.PUBLISHED)) {
                event.setState(EventState.CANCELED);
            } else {
                throw new EventStateException("Event can be reject only if it not PUBLISHED");
            }
        }
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
        EventFullDto updatedEvent = eventMapper.toEventFullDto(eventRepository.save(event));
        log.info("Event with id {} updated", updatedEvent.getId());
        return updatedEvent;
    }

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new RequestException("The user cannot send a request to their Event");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new RequestException("Request should be only for Published Event");
        }
        if (event.getConfirmedRequests() == event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new RequestException("Limit of participant requests has been reached");
        }
        ParticipationRequest request = new ParticipationRequest();
        request.setEvent(event);
        request.setRequester(userId);
        request.setCreated(LocalDateTime.now());
        if (!event.getRequestModeration()) {
            request.setStatus(RequestState.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else {
            request.setStatus(RequestState.PENDING);
        }
        request = requestRepository.save(request);
        log.info("Request id = {} created", request.getId());
        return requestMapper.toParticipationRequestDto(request);
    }

    @Override
    public Set<EventDto> getEvents(Set<Long> events) {
        Set<Event> eventSet = eventRepository.findAllByIdIn(events);
        return eventMapper.toEventDto(eventSet);
    }

    @Override
    // TODO: Только доступные onlyAvailable
    // TODO: Сортировка
    public List<EventShortDto> getEventsForUserByParameters(
            String text, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean paid,
            Boolean onlyAvailable, String sort, int from, int size) {
        List<Event> events;
        QEvent event = QEvent.event;
        Predicate predicate = QPredicates.builder()
                .add(text, event.annotation::containsIgnoreCase)
//                .add(text, event.description::containsIgnoreCase) TODO: должно быть "или"
                .add(categories, event.category.id::in)
                .add(rangeStart, event.eventDate::after)
                .add(rangeEnd, event.eventDate::before)
                .add(paid, event.paid::eq)
                .add(EventState.PUBLISHED, event.state::eq)
                .buildAnd();
        if (predicate == null) {
            events = eventRepository.findAll(PageRequest.of(from / size, size)).toList();
        } else {
            events = eventRepository.findAll(predicate, PageRequest.of(from / size, size)).toList();
        }
        return eventMapper.toEventShortDto(events);
    }

    @Override
    public EventFullDto getUserEventById(Long id) {
        Event event = eventRepository.findByIdAndStateIs(id, EventState.PUBLISHED);//, EventState.PUBLISHED.name());
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getRequestsByEventAndUser(Long eventId, Long userId) {
        Optional.ofNullable(eventRepository.findByIdAndInitiatorId(eventId, userId)).orElseThrow();
        List<ParticipationRequest> requests = requestRepository.findAllByEvent_Id(eventId);
        log.info("Participation Requests for event with id = {} got", eventId);
        return requestMapper.toParticipationRequestDto(requests);
    }
}
