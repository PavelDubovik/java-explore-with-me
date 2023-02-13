package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.handler.RequestException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.dto.RequestState;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.repository.RequestRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImp implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestMapper;

    @Override
    public List<ParticipationRequestDto> getByUserId(Long userId) {
        return requestMapper.toParticipationRequestDto(requestRepository.findAllByRequester(userId));
    }

    @Override
    public EventRequestStatusUpdateResult updateRequests(Long userId,
                                                         Long eventId,
                                                         EventRequestStatusUpdateRequest updateRequest) {

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId);

        if (event.getConfirmedRequests() == event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new RequestException("Limit of participant requests has been reached");
        }

        List<ParticipationRequestDto> confirmed = new ArrayList<>();
        List<ParticipationRequestDto> rejected = new ArrayList<>();

        requestRepository.findAllByEventIdAndRequester(event, updateRequest.getRequestIds())
                .stream()
                .peek(req -> {
                    System.out.println(req);
                    System.out.println(req.getStatus());
                    if (req.getStatus().equals(RequestState.PENDING)) {
                        if (updateRequest.getStatus().equals(RequestState.CONFIRMED)) {
                            if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                                req.setStatus(RequestState.CONFIRMED);
                                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                                eventRepository.save(event);
                            } else {
                                req.setStatus(RequestState.REJECTED);
                            }
                        } else {
                            req.setStatus(RequestState.REJECTED);
                        }
                    } else {
                        throw new RequestException("Request status not PENDING");
                    }
                })
                .map(requestMapper::toParticipationRequestDto)
                .forEach(t -> {
                    if (t.getStatus().equals(RequestState.CONFIRMED)) {
                        confirmed.add(t);
                    } else {
                        rejected.add(t);
                    }
                });

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmed)
                .rejectedRequests(rejected)
                .build();
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        ParticipationRequest request = Optional.of(requestRepository.findByIdAndRequester(requestId, userId)).orElseThrow();
        request.setStatus(RequestState.CANCELED);
        return requestMapper.toParticipationRequestDto(request);
    }
}
