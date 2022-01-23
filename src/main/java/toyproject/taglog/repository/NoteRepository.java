package toyproject.taglog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import toyproject.taglog.entity.Note;

import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    Optional<Note> findByIdAndDelYn(@Param("id") Long noteId, @Param("delYn") String delYN);

    Optional<Note> findByIdAndUserIdAndDelYn(@Param("id") Long noteId, @Param("userId") Long userId, @Param("delYn") String delYN);

    Optional<Note> findNoteById(Long noteId);

    @Query("select N from Note N join fetch N.user U where N.id = :id and N.delYn = 'N' ")
    Note findNoteAndUserById(@Param("id") Long noteId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Note N set N.delYn = 'Y', N.category.id = null where N.category.id = :categoryId")
    void bulkDeleteNoteByCategoryId(@Param("categoryId") Long categoryId);
}
