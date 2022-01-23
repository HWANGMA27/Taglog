package toyproject.taglog.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import toyproject.taglog.entity.Note;
import toyproject.taglog.repository.condition.NoteSearchCondition;

import java.util.List;

import static toyproject.taglog.entity.QCategory.category;
import static toyproject.taglog.entity.QNote.note;
import static toyproject.taglog.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class NoteDSLRepository {
    private final JPAQueryFactory queryFactory;

    public List<Note> findNotesWithCondition(NoteSearchCondition condition, Pageable pageable){
        return queryFactory
                .selectFrom(note)
                .where(userIdEq(condition.getUserId()),
                        noteIdEq(condition.getNoteId()),
                        categoryIdEq(condition.getCategoryId()),
                        delYnEq(condition.getDelYN())
                )
                .join(note.user, user).fetchJoin()
                .join(note.category, category).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public long countNotesWithCondition(NoteSearchCondition condition){
        return queryFactory
                .select(note.count())
                .from(note)
                .where(userIdEq(condition.getUserId()),
                        noteIdEq(condition.getNoteId()),
                        categoryIdEq(condition.getCategoryId()),
                        delYnEq(condition.getDelYN())
                )
                .fetch().get(0);
    }

    private BooleanExpression delYnEq(String delYN) {
        return StringUtils.hasText(delYN) ? note.delYn.eq(delYN) : null;
    }

    private BooleanExpression categoryIdEq(Long categoryId) {
        return categoryId != null ? category.id.eq(categoryId) : null;
    }

    private BooleanExpression noteIdEq(Long noteId) {
        return noteId != null ? note.id.eq(noteId) : null;
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId != null ? user.id.eq(userId) : null;
    }


}
