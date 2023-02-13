package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.ewm.event.dto.EventState;
import ru.practicum.ewm.event.model.Event;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface EventRepository extends PagingAndSortingRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    Event findByIdAndInitiatorId(Long eventId, Long userId);

    Set<Event> findAllByIdIn(Collection<Long> eventsId);

    Event findByIdAndStateIs(Long id, EventState state);
}
