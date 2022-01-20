package toyproject.taglog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import toyproject.taglog.entity.Category;
import toyproject.taglog.entity.User;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.user = :user ORDER BY order")
    List<Category> findByUser(@Param("user") User user);

    Optional<Category> getByUserIdAndId(@Param("userId") Long userId, @Param("id") Long id);
}
