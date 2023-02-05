package ru.practicum.ewm.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

public interface UserRepository extends PagingAndSortingRepository<UserDto, Long> {
    List<UserDto> findAllByOrderById(Pageable pageable);

    List<UserDto> findAllByIdInOrderById(List<Long> ids, Pageable pageable);
}
