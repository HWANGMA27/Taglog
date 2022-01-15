package toyproject.taglog.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.taglog.dto.CategoryDTO;
import toyproject.taglog.entity.Category;
import toyproject.taglog.entity.User;
import toyproject.taglog.repository.CategoryRepository;
import toyproject.taglog.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.List;

import static toyproject.taglog.entity.QCategory.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final EntityManager em;
    private JPAQueryFactory jpaQueryFactory;

    public List<Category> findCategoryByUserId(Long userId){
        User user = userRepository.findById(userId).get();
        return categoryRepository.findByUser(user);
    }

    @Transactional
    public List<Category> updateCategory(CategoryDTO categoryDTO){
        Category category = categoryRepository.getByUserIdAndId(categoryDTO.getUserId(), categoryDTO.getCategoryId());
        int order = categoryDTO.getOrder();
        List<Category> reOrderList = findCategoryGoeOrder(categoryDTO.getUserId(), order);
        addOrderCategories(reOrderList, order);
        category.updateCategory(categoryDTO.getName(), order);
        return findCategoryByUserId(categoryDTO.getUserId());
    }

    @Transactional
    public List<Category> deleteCategory(Long userId, Long category_id) {
        Category category = categoryRepository.getByUserIdAndId(userId, category_id);
        List<Category> reOrderList = findCategoryGoeOrder(userId, category.getOrder());
        minusOrderCategories(reOrderList);
        categoryRepository.delete(category);
        return findCategoryByUserId(userId);
    }

    @Transactional
    public List<Category> addCategory(CategoryDTO categoryDTO) {
        User user = userRepository.findById(categoryDTO.getUserId()).get();
        List<Category> reOrderList =  findCategoryGoeOrder(categoryDTO.getUserId(), 1);
        addOrderCategories(reOrderList, 1);
        Category category = new Category(categoryDTO.getName(), 1, user);
        categoryRepository.save(category);
        return findCategoryByUserId(categoryDTO.getUserId());
    }

    private List<Category> findCategoryGoeOrder(@NotNull Long userId, int order){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<Category> categoryList = queryFactory
                .selectFrom(category)
                .where(category.user.id.eq(userId), category.order.goe(order))
                .fetch();
        return categoryList;
    }

    private void addOrderCategories(List<Category> categories, int offset){
        int reorder = offset;
        for (Category category : categories) {
            reorder += 1;
            category.reOrderCategory(reorder);
            categoryRepository.save(category);
        }
    }

    private void minusOrderCategories(List<Category> categories){
        for (Category category : categories) {
            category.reOrderCategory(category.getOrder()-1);
            categoryRepository.save(category);
        }
    }
}
