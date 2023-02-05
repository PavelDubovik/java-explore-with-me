package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.repository.CategoryRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        CategoryDto categoryDto = categoryMapper.toCategoryDto(newCategoryDto);
        categoryDto = categoryRepository.save(categoryDto);
        log.info("Category with id = {} and name = {} is created", categoryDto.getId(), categoryDto.getName());
        return categoryDto;
    }

    @Override
    public void deleteCategory(Long catId) {
        categoryRepository.deleteById(catId);
        log.info("Category with id = {} is deleted", catId);
    }

    @Override
    public CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto) {
        CategoryDto categoryDto = categoryMapper.toCategoryDto(newCategoryDto);
        categoryDto.setId(catId);
        categoryDto = categoryRepository.save(categoryDto);
        log.info("Category with id = {} is updated", categoryDto.getId());
        return categoryDto;
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        // TODO: проверить на положительное значение from и size
        Pageable pageable = PageRequest.of(from, size);
        List<CategoryDto> categoryDtoList = categoryRepository.findAll(pageable).toList();
        log.info("{} categories got", categoryDtoList.size());
        return categoryDtoList;
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        CategoryDto categoryDto = categoryRepository.findById(catId).orElseThrow();
        log.info("Category with id = {} got", catId);
        return categoryDto;
    }
}
