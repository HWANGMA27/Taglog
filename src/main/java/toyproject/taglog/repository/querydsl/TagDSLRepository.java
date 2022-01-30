package toyproject.taglog.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import toyproject.taglog.entity.Tag;

import java.util.List;

import static toyproject.taglog.entity.QNoteTag.noteTag;
import static toyproject.taglog.entity.QTag.tag;
import static toyproject.taglog.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class TagDSLRepository {

    private final JPAQueryFactory queryFactory;

    public List<Tag> findTagByUserId(Long userId) {
        return queryFactory
                .select(noteTag.tag)
                .from(noteTag)
                .join(noteTag.tag, tag)
                .join(noteTag.note.user, user)
                .where(user.id.eq(userId))
                .fetch();
    }

    public List<Tag> findTagByNoteId(Long noteId) {
        return queryFactory
                .select(noteTag.tag)
                .from(noteTag)
                .join(noteTag.tag, tag)
                .where(noteTag.note.id.eq(noteId))
                .fetch();
    }
}
