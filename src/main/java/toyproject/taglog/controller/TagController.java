package toyproject.taglog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toyproject.taglog.dto.NoteDTO;
import toyproject.taglog.dto.TagDTO;
import toyproject.taglog.entity.NoteTag;
import toyproject.taglog.service.NoteService;
import toyproject.taglog.service.NoteTagService;
import toyproject.taglog.service.TagService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tag")
public class TagController {

    private final TagService tagService;
    private final NoteTagService noteTagService;
    private final NoteService noteService;

    @GetMapping("/all/{id}")
    public List<TagDTO> findAllTag(@PathVariable("id") Long userId){
        List<NoteTag> noteTagList = noteTagService.findNoteTagByUserId(userId);
        return noteTagList.stream()
                .map(NoteTag::getTag)
                .map(tag -> new TagDTO(tag.getId(), tag.getName()))
                .distinct()
                .collect(Collectors.toList());
    }

    @GetMapping("/{tag_id}/user/{id}")
    public List<NoteDTO> findNoteByTag(@PathVariable("id") Long userId,
                                       @PathVariable("tag_id") Long tagId){
        return noteService.findNoteByTag(userId, tagId);
    }
}
