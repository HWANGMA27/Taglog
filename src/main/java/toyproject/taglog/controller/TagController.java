package toyproject.taglog.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toyproject.taglog.apiutills.ApiResult;
import toyproject.taglog.apiutills.ApiUtils;
import toyproject.taglog.dto.NoteDTO;
import toyproject.taglog.dto.TagDTO;
import toyproject.taglog.service.NoteService;
import toyproject.taglog.service.NoteTagService;
import toyproject.taglog.service.TagService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tag")
@ApiOperation(value = "Tag API")
public class TagController {

    private final TagService tagService;
    private final NoteTagService noteTagService;
    private final NoteService noteService;

    @Operation(summary = "태그 전체 조회", description = "회원 Id로 태그를 전체 조회합니다.")
    @GetMapping("/all/{id}")
    public ApiResult<List<TagDTO>> findAllTag(@Parameter(description = "회원 Id", in = ParameterIn.PATH) @PathVariable("id") Long userId){
        return ApiUtils.success(tagService.findTagByUserId(userId)
                .stream()
                .map(tag -> TagDTO.builder()
                                .id(tag.getId())
                                .name(tag.getName())
                                .build())
                .distinct()
                .collect(Collectors.toList()));
    }

    @Operation(summary = "태그된 노트 조회", description = "회원 Id와 태그 Id로 노트를 조회합니다.")
    @GetMapping("/{tag_id}/user/{id}")
    public ApiResult<List<NoteDTO>> findNoteByTag(@Parameter(description = "회원 Id", in = ParameterIn.PATH) @PathVariable("id") Long userId,
                                       @Parameter(description = "태그 Id", in = ParameterIn.PATH) @PathVariable("tag_id") Long tagId){
        return ApiUtils.success(noteService.findNoteByTag(userId, tagId));
    }
}
