package toyproject.taglog.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import toyproject.taglog.entity.*;
import toyproject.taglog.repository.condition.NoteSearchCondition;
import toyproject.taglog.repository.querydsl.NoteDSLRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@SpringBootTest
@Transactional
class NoteRepositoryTest {
    @Autowired
    NoteRepository noteRepository;
    @Autowired
    NoteDSLRepository noteDSLRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    NoteTagRepository noteTagRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @PersistenceContext
    EntityManager em;

    User user;
    Category category;
    Note note;

    @BeforeEach
    public void beforeEach() throws Exception{
        user = new User("email", "name", "picture", Role.USER);
        userRepository.save(user);

        category = new Category("테스트 카테고리", 2, user);
        categoryRepository.save(category);

        Tag tag = new Tag("테스트 태그");
        Tag tag2 = new Tag("테스트 태그2");
        tagRepository.save(tag);
        tagRepository.save(tag2);

        note = new Note("타이틀", "컨텐츠");
        note.updateCategory(category);
        note.updateNoteStatus("N");
        note.updateUser(user);
        noteRepository.save(note);

        NoteTag noteTag = new NoteTag(note, tag);
        NoteTag noteTag2 = new NoteTag(note, tag2);
        noteTagRepository.save(noteTag);
        noteTagRepository.save(noteTag2);

        em.flush();
        em.clear();
    }

    @DisplayName("사용자의 노트 갯수 테스트")
    @Test
    public void countNoteListSizeTest() throws Exception{
        //when
        NoteSearchCondition condition = new NoteSearchCondition();
        condition.setUserId(user.getId());
        long noteListSize = noteDSLRepository.countNotesWithCondition(condition);
        //then
        Assertions.assertThat(noteListSize).isEqualTo(1);
    }



    @DisplayName("Id로 노트 찾기 테스트")
    @Test
    public void findByIdAndUserIdTest() throws Exception{
        //when
        Optional<Note> findNote = noteRepository.findByIdAndDelYn(note.getId(), "N");
        //then
        Assertions.assertThat(findNote.isEmpty()).isFalse();
        Note note = findNote.get();
        Assertions.assertThat(note.getTitle())
                .isEqualTo("타이틀");
    }

    @DisplayName("노트 벌크 삭제 테스트")
    @Test
    public void bulkDeleteTest() throws  Exception{
        //given
        Category newCategory = new Category("테스트 카테고리", 2, user);
        categoryRepository.save(newCategory);
        for (int i = 0; i < 100; i++) {
            Note newNote = new Note("타이틀", "컨텐츠");
            newNote.updateCategory(newCategory);
            newNote.updateNoteStatus("N");
            newNote.updateUser(user);
            noteRepository.save(newNote);
        }
        em.flush();
        em.clear();

        //when
        noteRepository.bulkDeleteNoteByCategoryId(newCategory.getId());
        long count = noteRepository.countNoteNotDeleted(newCategory.getId());
        long otherCategoryCount = noteRepository.countNoteNotDeleted(category.getId());

        //then
        //다른 카테고리의 노트는 삭제되면 안된다
        Assertions.assertThat(count).isEqualTo(0);
        Assertions.assertThat(otherCategoryCount).isEqualTo(1);
    }
}