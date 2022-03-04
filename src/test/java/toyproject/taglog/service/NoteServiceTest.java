package toyproject.taglog.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import toyproject.taglog.dto.NoteDTO;
import toyproject.taglog.entity.*;
import toyproject.taglog.exception.invalid.InvalidateNoteException;
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
    Tag tag, tag2;

    @BeforeEach
    public void beforeEach() {
        user = new User("email", "name", "picture", Role.USER);
        userRepository.save(user);

        category = new Category("테스트 카테고리", 2, user);
        categoryRepository.save(category);

        tag = new Tag("테스트 태그");
        tag2 = new Tag("테스트 태그2");
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

    @DisplayName("노트 삭제 테스트")
    @Test
    public void deleteNoteTest() {
        //when
        Long noteId = note.getId();
        noteService.deleteNote(user.getId(), noteId);
        em.clear();
        em.flush();

        List<NoteTag> noteTagByNoteId = noteTagService.findNoteTagByNoteId(noteId);

        //then
        Assertions.assertThrows(InvalidateNoteException.class, () -> noteService.findNoteByIdAndDelYn(note.getId(), "N"));
        //노트 삭제 후 연결된 중간테이블 데이터 삭제 확인
        org.assertj.core.api.Assertions.assertThat(noteTagByNoteId.size())
                .isEqualTo(0);
    }

    @DisplayName("노트 추가 테스트")
    @Test
    public void addNoteTest() {
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
    public void updateNoteCategoryTest() {
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

    @DisplayName("노트 컨텐츠 변경 테스트")
    @Test
    public void updateNoteTest() {
        //given
        note.updateContents("바뀐 타이틀", "바뀐 컨텐츠");
        ArrayList<Tag> tags = new ArrayList<>();
        noteService.updateNote(note, note.getId(), user.getId(), category.getId(), tags);
        em.flush();
        em.clear();

        //when
        Note findNote = noteRepository.findById(note.getId()).get();

        //then
        org.assertj.core.api.Assertions.assertThat(findNote.getTitle()).isEqualTo(note.getTitle());
        org.assertj.core.api.Assertions.assertThat(findNote.getContents()).isEqualTo(note.getContents());

    }

    @DisplayName("선택한 태그가 있는 노트만 가져오는 테스트")
    @Test
    public void findNoteByTagTest() {
        //given
        Tag newTag = new Tag("새로운 태그");
        tagRepository.save(newTag);

        note = new Note("타이틀", "컨텐츠");
        note.updateCategory(category);
        note.updateNoteStatus("N");
        note.updateUser(user);
        noteRepository.save(note);

        NoteTag noteTag = new NoteTag(note, newTag);
        noteTagRepository.save(noteTag);

        em.flush();
        em.clear();

        Pageable pageable = PageRequest.of(0,5);
        //when
        Slice<NoteDTO> noteByTag = noteService.findNoteByTag(user.getId(), newTag.getId(), pageable);
        //then
        org.assertj.core.api.Assertions.assertThat(noteByTag.getNumberOfElements()).isEqualTo(1);
    }
}