package ru.practicum.ewm.categories;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.categories.dto.NewCategoryDto;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        return CategoryMapper.categoryToDto(repository.save(CategoryMapper.dtoToCategory(newCategoryDto)));
    }

    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long id) {
        return CategoryMapper.categoryToDto(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория не найдена")));
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        return repository.findAll(PageRequest.of(from / size, size)).stream()
                .map(CategoryMapper::categoryToDto)
                .collect(Collectors.toList());
    }

    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));
        category.setName(categoryDto.getName());
        return CategoryMapper.categoryToDto(repository.save(category));
    }

    public void deleteCategory(Long id) {
        if (repository.findById(id).isPresent()) {
            repository.delete(repository.findById(id).get());
        } else {
            throw new NotFoundException("Категория не найдена");
        }

    }
}
