package ru.practicum.ewm.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.category.dto.CategoryDto;

public interface CategoryRepository extends JpaRepository<CategoryDto, Long> {
}
