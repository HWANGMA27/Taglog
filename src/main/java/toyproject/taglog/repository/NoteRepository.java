package toyproject.taglog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import toyproject.taglog.entity.Note;

import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    Optional<Note> findByIdAndDelYn(@Param("id") Long noteId, @Param("delYn") String delYN);

    long countByUserId(Long userId);

    Optional<Note> findByIdAndUserIdAndDelYn(@Param("id") Long noteId, @Param("userId") Long userId, @Param("delYn") String delYN);

    long countByUserIdAndCategoryId(Long userId, Long categoryId);

    Optional<Note> getNoteById(Long noteId);
}
