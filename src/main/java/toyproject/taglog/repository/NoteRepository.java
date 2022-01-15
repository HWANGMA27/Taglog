package toyproject.taglog.repository;

import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import toyproject.taglog.entity.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {
    long countByUserId(Long userId);
}
