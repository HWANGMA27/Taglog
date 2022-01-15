package toyproject.taglog.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import toyproject.taglog.dto.CategoryDTO;
import toyproject.taglog.entity.Category;
import toyproject.taglog.entity.Role;
import toyproject.taglog.entity.User;
import toyproject.taglog.repository.CategoryRepository;
import toyproject.taglog.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @PersistenceContext
    private EntityManager em;
    User user;
    Category category;

    @BeforeEach
    public void beforeEach() throws Exception{
        user = new User("email", "name", "picture", Role.USER);
        userRepository.save(user);

        category = new Category("테스트 카테고리", 1, user);
        Category category2 = new Category("테스트 카테고리2", 2, user);
        categoryRepository.save(category);
        categoryRepository.save(category2);
        em.flush();
        em.clear();
    }

    @Test
    public void findMemberTest() throws Exception{
        //when
        List<Category> result = categoryService.findCategoryByUserId(user.getId());
        //then
        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void addCategoryTest() throws Exception{
        //given
        CategoryDTO category = new CategoryDTO(null, "테스트 카테고리3", 0, user.getId());
        categoryService.addCategory(category);
        em.flush();
        em.clear();

        CategoryDTO category2 = new CategoryDTO(null, "테스트 카테고리4", 0, user.getId());
        categoryService.addCategory(category2);
        em.flush();
        em.clear();

        //when
        List<Category> result = categoryService.findCategoryByUserId(user.getId());

        //then
        Assertions.assertThat(result.size()).isEqualTo(4);
        Assertions.assertThat(result)
                .extracting("name")
                .contains("테스트 카테고리3", "테스트 카테고리4");
    }

    @Test
    public void deleteCategoryTest() throws Exception{
        //given
        Long userId = user.getId();
        Long categoryId = category.getId();
        categoryService.deleteCategory(userId, categoryId);
        em.flush();
        em.clear();

        //when
        List<Category> result = categoryService.findCategoryByUserId(user.getId());

        //then
        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result)
                .extracting("name")
                .doesNotContain("테스트 카테고리");
    }

    @Test
    public void updateCategoryTest() throws Exception{
        //given
        category.updateCategory("업데이트", 10);
        CategoryDTO categoryDTO = new CategoryDTO(category.getId(), category.getName(), category.getOrder(), category.getUser().getId());
        categoryService.updateCategory(categoryDTO);
        em.flush();
        em.clear();

        //when
        List<Category> categoryByUserId = categoryService.findCategoryByUserId(user.getId());

        //then
        Assertions.assertThat(categoryByUserId)
                .extracting("name")
                .contains("업데이트")
                .doesNotContain("테스트 카테고리");
    }
}