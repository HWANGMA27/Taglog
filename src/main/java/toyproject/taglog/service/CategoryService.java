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

    public List<Category> findCategoryByUserId(Long userId){
        User user = validateService.validateUser(userId);
        return categoryRepository.findByUser(user);
    }

    @Transactional
    public List<Category> updateCategory(CategoryDTO categoryDTO){
        Long categoryId = categoryDTO.getCategoryId();
        Category category = validateService.validateCategory(categoryId);
        int order = categoryDTO.getOrder();
        List<Category> reOrderList = findCategoryGoeOrder(categoryId, order);
        addOrderCategories(reOrderList);
        category.updateCategory(categoryDTO.getName(), order);
        return findCategoryByUserId(categoryDTO.getUserId());
    }

    @Transactional
    public List<Category> deleteCategory(Long userId, Long categoryId) {
        noteRepository.bulkDeleteNoteByCategoryId(categoryId);
        Category category = validateService.validateCategory(categoryId);
        List<Category> reOrderList = findCategoryGoeOrder(userId, category.getOrder());
        minusOrderCategories(reOrderList);
        categoryRepository.delete(category);
        //노트 삭제 로직 추가
        return findCategoryByUserId(userId);
    }

    @Transactional
    public List<Category> addCategory(CategoryDTO categoryDTO) {
        User user = validateService.validateUser(categoryDTO.getUserId());
        List<Category> reOrderList =  findCategoryGoeOrder(categoryDTO.getUserId(), 1);
        addOrderCategories(reOrderList);

        Category category = new Category(categoryDTO.getName(), 1, user);
        categoryRepository.save(category);
        return findCategoryByUserId(categoryDTO.getUserId());
    }

    private List<Category> findCategoryGoeOrder(@NotNull Long userId, int order){
        CategorySearchCondition condition = new CategorySearchCondition();
        condition.setUserId(userId);
        return categoryDSLRepository.findCategoryWithCondition(condition);
    }

    private void addOrderCategories(List<Category> categories){
        for (Category category : categories) {
            category.reOrderCategory(category.getOrder()+1);
        }
    }

    private void minusOrderCategories(List<Category> categories){
        for (Category category : categories) {
            category.reOrderCategory(category.getOrder()-1);
        }
    }
}
