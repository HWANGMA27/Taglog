package toyproject.taglog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.taglog.dto.CategoryDTO;
import toyproject.taglog.entity.Category;
import toyproject.taglog.entity.User;
import toyproject.taglog.repository.CategoryRepository;
import toyproject.taglog.repository.NoteRepository;
import toyproject.taglog.repository.condition.CategorySearchCondition;
import toyproject.taglog.repository.querydsl.CategoryDSLRepository;
import toyproject.taglog.service.common.ValidateService;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryDSLRepository categoryDSLRepository;
    private final NoteRepository noteRepository;
    private final ValidateService validateService;

    public List<Category> findCategoryByUserId(Long userId) {
        User user = validateService.validateUser(userId);
        return categoryRepository.findByUser(user);
    }

    @Transactional
    public List<Category> updateCategory(CategoryDTO categoryDTO) {
        Long categoryId = categoryDTO.getCategoryId();
        Category category = validateService.validateCategory(categoryId);

        int requestOrder = categoryDTO.getOrder();
        if (requestOrder != category.getOrder()) {
            //order 변경
            Long userId = categoryDTO.getUserId();
            categoryDSLRepository.relocateOrder(userId, requestOrder, 1);
            category.relocateCategory(requestOrder);
        } else {
            category.updateCategoryName(categoryDTO.getName());
        }

        return findCategoryByUserId(categoryDTO.getUserId());
    }

    @Transactional
    public List<Category> deleteCategory(Long userId, Long categoryId) {
        validateService.validateUser(userId);
        noteRepository.bulkDeleteNoteByCategoryId(categoryId);
        Category category = validateService.validateCategory(categoryId);
        categoryDSLRepository.relocateOrder(userId, 1, -1);
        categoryRepository.delete(category);
        //노트 삭제 로직 추가
        return findCategoryByUserId(userId);
    }

    @Transactional
    public List<Category> addCategory(CategoryDTO categoryDTO) {
        Long userId = categoryDTO.getUserId();
        User user = validateService.validateUser(userId);
        categoryDSLRepository.relocateOrder(userId, 1, 1);

        Category category = new Category(categoryDTO.getName(), 1, user);
        categoryRepository.save(category);
        return findCategoryByUserId(categoryDTO.getUserId());
    }

    private List<Category> findCategoryGoeOrder(@NotNull Long userId, int order) {
        CategorySearchCondition condition = new CategorySearchCondition();
        condition.setUserId(userId);
        return categoryDSLRepository.findCategoryWithCondition(condition);
    }
}
