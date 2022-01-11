package toyproject.taglog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.taglog.dto.CategoryDTO;
import toyproject.taglog.entity.Category;
import toyproject.taglog.entity.User;
import toyproject.taglog.repository.CategoryRepository;
import toyproject.taglog.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public List<Category> findCategoryByUserId(Long userId){
        User user = userRepository.findById(userId).get();
        return categoryRepository.findByUser(user);
    }

    @Transactional
    public List<Category> updateCategory(CategoryDTO categoryDTO){
        Category category = categoryRepository.getByUserIdAndId(categoryDTO.getUserId(), categoryDTO.getCategoryId());
        category.updateCategory(categoryDTO.getName(), categoryDTO.getOrder());
        return findCategoryByUserId(categoryDTO.getUserId());
    }


    @Transactional
    public List<Category> deleteCategory(Long userId, Long category_id) {
        Category category = categoryRepository.getByUserIdAndId(userId, category_id);
        categoryRepository.delete(category);
        return findCategoryByUserId(userId);
    }

    @Transactional
    public List<Category> addCategory(CategoryDTO categoryDTO) {
        User user = userRepository.findById(categoryDTO.getUserId()).get();
        List<Category> categories = user.getCategoryList();
        reOrderCategories(categories);
        Category category = new Category(categoryDTO.getName(), 1, user);
        categoryRepository.save(category);
        return findCategoryByUserId(categoryDTO.getUserId());
    }

    private void reOrderCategories(List<Category> categories){
        for (Category category : categories) {
            category.reOrderCategory(category.getOrder()+1);
            categoryRepository.save(category);
        }
    }
}
