package toyproject.taglog.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import toyproject.taglog.entity.Category;
import toyproject.taglog.repository.condition.CategorySearchCondition;

import java.util.List;

import static toyproject.taglog.entity.QCategory.category;
import static toyproject.taglog.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class CategoryDSLRepository {
    private final JPAQueryFactory queryFactory;

    public List<Category> findCategoryWithCondition (CategorySearchCondition condition){
        return queryFactory
                .selectFrom(category)
                .where(userIdEq(condition.getUserId()),
                        categoryGoe(condition.getOrder()))
                .join(category.user, user).fetchJoin()
                .fetch();
    }
    private BooleanExpression categoryGoe(Integer order){
        return order != null ? category.order.goe(order) : null;
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId != null ? user.id.eq(userId) : null;
    }
}
