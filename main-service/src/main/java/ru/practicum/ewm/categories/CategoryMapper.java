package ru.practicum.ewm.categories;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.categories.dto.NewCategoryDto;
import ru.practicum.ewm.categories.model.Category;

@UtilityClass
public class CategoryMapper {

    public Category dtoToCategory(NewCategoryDto newCategoryDto) {
        return new Category(newCategoryDto.getName());
    }

    public Category dtoToCategory(CategoryDto categoryDto) {
        return new Category(categoryDto.getId(), categoryDto.getName());
    }

    public CategoryDto categoryToDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
