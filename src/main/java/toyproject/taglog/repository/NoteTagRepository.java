package toyproject.taglog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toyproject.taglog.entity.NoteTag;

public interface NoteTagRepository extends JpaRepository<NoteTag, Long> {
}
