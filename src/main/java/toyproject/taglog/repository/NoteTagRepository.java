package toyproject.taglog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import toyproject.taglog.entity.NoteTag;

import java.util.List;

public interface NoteTagRepository extends JpaRepository<NoteTag, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from NoteTag NT where NT.note.id = :noteId")
    void bulkDelete(@Param("noteId") Long noteId);

    List<NoteTag> findNoteTagByNoteId(Long noteId);

}
