package toyproject.taglog.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import toyproject.taglog.entity.*;
import toyproject.taglog.exception.NoteNotFoundException;
import toyproject.taglog.repository.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@Transactional
class NoteServiceTest {
    @Autowired
    NoteTagService noteTagService;
    @Autowired
    NoteService noteService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    NoteTagRepository noteTagRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    NoteRepository noteRepository;
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

        NoteTag noteTag = new NoteTag(note, tag, user);
        NoteTag noteTag2 = new NoteTag(note, tag2, user);
        noteTagRepository.save(noteTag);
        noteTagRepository.save(noteTag2);

        em.flush();
        em.clear();
    }

    @DisplayName("노트 삭제 테스트")
    @Test
    public void deleteNoteTest() throws Exception{
        //when
        Long noteId = note.getId();
        noteService.deleteNote(user.getId(), noteId);
        em.clear();
        em.flush();

        List<NoteTag> noteTagByNoteId = noteTagService.findNoteTagByNoteId(noteId);

        //then
        Assertions.assertThrows(NoteNotFoundException.class, () -> {
            noteService.findByIdAndDelYn(note.getId(), "N");
        });
        //노트 삭제 후 연결된 중간테이블 데이터 삭제 확인
        org.assertj.core.api.Assertions.assertThat(noteTagByNoteId.size())
                .isEqualTo(0);
    }

    @DisplayName("노트 추가 테스트")
    @Test
    public void addNoteTest() throws Exception{
        //given
        Note note = new Note("추가 테스트", "추가테스트 컨텐츠");
        Long userId = user.getId();
        Long categoryId = category.getId();
        List<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag("추가태그"));
        tagList.add(new Tag("추가태그2"));
        noteService.addNote(note, userId, categoryId, tagList);
        em.flush();
        em.clear();

        //when
        Note findById = noteRepository.findById(note.getId()).get();

        //then
        org.assertj.core.api.Assertions.assertThat(findById.getTitle()).isEqualTo(note.getTitle());
        org.assertj.core.api.Assertions.assertThat(findById.getDelYn()).isEqualTo("N");
    }

    @DisplayName("노트 카테고리 변경 테스트")
    @Test
    public void updateNoteCategoryTest() throws Exception{
        //given
        Category newCategory = new Category("테스트용 카테고리", user);
        categoryRepository.save(newCategory);

        noteService.updateNoteCategory(note.getId(), newCategory.getId());
        em.flush();
        em.clear();

        //when
        Note findNote = noteRepository.findById(note.getId()).get();

        //then

        org.assertj.core.api.Assertions.assertThat(findNote.getCategory().getId()).isEqualTo(newCategory.getId());
        org.assertj.core.api.Assertions.assertThat(findNote.getCategory().getName()).isEqualTo(newCategory.getName());
    }

    @DisplayName("노트 없는 카테고리 변경 예외처리 테스트")
    @Test
    public void updateNoteNotExistCategoryTest() throws Exception{
        //given

        //when

        //then
    }

    @DisplayName("노트 컨텐츠 변경 테스트")
    @Test
    public void updateNoteTest() throws Exception{
        //given
        note.updateContents("바뀐 타이틀", "바뀐 컨텐츠");
        noteService.updateNote(note, note.getId(), user.getId(), category.getId(), null);
        em.flush();
        em.clear();

        //when
        Note findNote = noteRepository.findById(note.getId()).get();

        //then
        org.assertj.core.api.Assertions.assertThat(findNote.getTitle()).isEqualTo(note.getTitle());
        org.assertj.core.api.Assertions.assertThat(findNote.getContents()).isEqualTo(note.getContents());

    }
}