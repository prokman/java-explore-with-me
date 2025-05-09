package ewm.categories.service;

import ewm.categories.dto.CategoryDto;
import ewm.categories.dto.NewCategoryDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public interface CatService {
    CategoryDto postCat(@Valid @NotNull NewCategoryDto request);

    CategoryDto patchCat(@Positive @NotNull Long catId, @Valid @NotNull NewCategoryDto catPatch);

    void deleteCat(@Positive @NotNull Long catId);

    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategoryById(@Positive @NotNull Long catId);
}
