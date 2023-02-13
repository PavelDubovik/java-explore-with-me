package ru.practicum.ewm.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    List<User> findAllByOrderById(Pageable pageable);

    List<User> findAllByIdInOrderById(List<Long> ids, Pageable pageable);
}
