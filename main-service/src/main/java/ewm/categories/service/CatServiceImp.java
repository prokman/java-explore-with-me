package ewm.categories.service;

import ewm.categories.dto.CategoryDto;
import ewm.categories.dto.CategoryMapper;
import ewm.categories.dto.NewCategoryDto;
import ewm.categories.model.Category;
import ewm.categories.repository.CatRepository;
import ewm.event.repository.EventRepository;
import ewm.exceptions.ConditionNotMeetException;
import ewm.exceptions.DuplicateDataException;
import ewm.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CatServiceImp implements CatService {
    private final CatRepository catRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto postCat(NewCategoryDto request) {
        Category category;
        try {
            category = catRepository.save(CategoryMapper.requestToCategory(request));
        } catch (DataIntegrityViolationException ex) {
            if (ex.getMessage().contains("name_uq")) {
                throw new DuplicateDataException("Name is busy");
            }
            throw ex;
        }
        return CategoryMapper.categoryToDto(category);
    }

    @Override
    @Transactional
    public CategoryDto patchCat(Long catId, NewCategoryDto catPatch) {
        Category patchedCat;
        if (catRepository.existsByNameAndId(catPatch.getName(), catId)) {
            return CategoryMapper.categoryToDto(catRepository.findById(catId)
                    .orElseThrow(() -> new NotFoundException("Category with ID " + catId + " not found")));
        }
        log.info("Before existsByName");
        if (catRepository.existsByName(catPatch.getName())) {
            throw new DuplicateDataException("Name is busy");
        }

        Category category = catRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with ID " + catId + " not found"));
        category.setName(catPatch.getName());

        try {
            patchedCat = catRepository.save(category);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getMessage().contains("name_uq")) {
                throw new DuplicateDataException("Name is busy");
            }
            throw ex;
        }

        return CategoryMapper.categoryToDto(patchedCat);
    }

    @Override
    @Transactional
    public void deleteCat(Long catId) {
        if (!catRepository.existsById(catId)) {
            throw new NotFoundException("Category with ID " + catId + " not found");
        }
        List<Long> eventIds = eventRepository.findIdsByCategoryId(catId);
        StringBuilder stringBuilder = new StringBuilder();
        for (Long ids : eventIds) {
            stringBuilder.append(ids);
            stringBuilder.append("; ");
        }
        String idsOfEvents = stringBuilder.toString();
        if (!eventIds.isEmpty()) {
            throw new ConditionNotMeetException("Category № " + catId + " связана с событиями № " + idsOfEvents);
        } else {
            catRepository.deleteById(catId);
        }
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);
        List<Category> categoryList = catRepository.getCatByParam(pageable);
        return categoryList.stream().map(CategoryMapper::categoryToDto).toList();
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        return catRepository.findById(catId)
                .map(CategoryMapper::categoryToDto)
                .orElseThrow(() -> new NotFoundException("катеогрии с ИД" + catId + " не найдено"));
    }
}
