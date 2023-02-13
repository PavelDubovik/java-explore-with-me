package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.request.model.ParticipationRequest;

import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    ParticipationRequest findByIdAndRequester(Long Id, Long userId);

    List<ParticipationRequest> findAllByRequester(Long id);

    List<ParticipationRequest> findAllByEvent_Id(Long eventId);

    @Query ("SELECT pr FROM ParticipationRequest pr WHERE pr.event = :event AND pr.id in :requestIds")
    List<ParticipationRequest> findAllByEventIdAndRequester (Event event, List<Long> requestIds);
}
