package toyproject.taglog.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.taglog.entity.Category;
import toyproject.taglog.entity.Note;
import toyproject.taglog.entity.User;
import toyproject.taglog.exception.invalid.InvalidateCategoryException;
import toyproject.taglog.exception.invalid.InvalidateNoteException;
import toyproject.taglog.exception.invalid.InvalidateUserException;
import toyproject.taglog.repository.CategoryRepository;
import toyproject.taglog.repository.NoteRepository;
import toyproject.taglog.repository.UserRepository;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ValidateService {
    private final NoteRepository noteRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public Note validateNote(Long noteId, String delYn){
        return noteRepository.findByIdAndDelYn(noteId, delYn).orElseThrow(InvalidateNoteException::new);
    }

    public Category validateCategory(Long categoryId){
        return categoryRepository.findById(categoryId).orElseThrow(InvalidateCategoryException::new);
    }

    public User validateUser(Long userId){
        return userRepository.findById(userId).orElseThrow(InvalidateUserException::new);
    }
}
