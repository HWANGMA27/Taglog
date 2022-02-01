package toyproject.taglog.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import toyproject.taglog.dto.CategoryDTO;
import toyproject.taglog.entity.Category;
import toyproject.taglog.entity.Role;
import toyproject.taglog.entity.User;
import toyproject.taglog.exception.invalid.InvalidateCategoryException;
import toyproject.taglog.repository.CategoryRepository;
import toyproject.taglog.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @PersistenceContext
    EntityManager em;

    User user;
    Category category;

    @BeforeEach
    public void beforeEach() throws Exception{
        user = new User("email", "name", "picture", Role.USER);
        userRepository.save(user);

        category = new Category("2번 카테고리", 2, user);
        Category category2 = new Category("1번 카테고리", 1, user);
        categoryRepository.save(category);
        categoryRepository.save(category2);
        em.flush();
        em.clear();
    }

    @DisplayName("사용자의 카테고리 출력 테스트")
    @Test
    public void findCategoryTest() throws Exception{
        //when
        List<Category> result = categoryService.findCategoryByUserId(user.getId());
        //then
        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @DisplayName("카테고리 추가 테스트")
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

    @DisplayName("카테고리 삭제 테스트")
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
                .doesNotContain("2번 카테고리");
    }

    @DisplayName("카테고리 이름 수정 테스트")
    @Test
    public void updateCategoryNameTest() throws Exception{
        //given
        category.updateCategoryName("업데이트");
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

    @DisplayName("카테고리 순서 수정 테스트")
    @Test
    public void relocateCategoryTest() throws Exception{
        //given
        category.relocateCategory(1);
        CategoryDTO categoryDTO = new CategoryDTO(category.getId(), category.getName(), category.getOrder(), category.getUser().getId());
        categoryService.updateCategory(categoryDTO);
        em.flush();
        em.clear();

        //when
        Category findCategory = categoryRepository.findById(category.getId()).orElseThrow(InvalidateCategoryException::new);
        Category oldCategory = categoryRepository.findByName("1번 카테고리").orElseThrow(InvalidateCategoryException::new);
        //then
        Assertions.assertThat(findCategory.getOrder()).isEqualTo(1);
        Assertions.assertThat(oldCategory.getOrder()).isEqualTo(2);
    }
}