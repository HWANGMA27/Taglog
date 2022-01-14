package toyproject.taglog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import toyproject.taglog.dto.NoteDTO;
import toyproject.taglog.service.NoteService;
import toyproject.taglog.service.TagService;

@RestController
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    private final TagService tagService;

    @GetMapping("/note/all/{user_id}")
    public Slice<NoteDTO> findNoteByUserId(@PathVariable("user_id") Long userId, Pageable pageable) {
        return noteService.findAllNoteByUserId(userId, pageable);
    }

//    @GetMapping("/note/{user_id}/{category_id}")
//    public Slice<NoteDTO> findNoteByCategory(@PathVariable("user_id") Long userId,
//                                             @PathVariable("category_id") Long categoryId){
//
//    }
}
