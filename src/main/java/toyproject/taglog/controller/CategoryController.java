package toyproject.taglog.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;
import toyproject.taglog.dto.CategoryDTO;
import toyproject.taglog.dto.NoteDTO;
import toyproject.taglog.entity.Category;
import toyproject.taglog.service.CategoryService;
import toyproject.taglog.service.NoteService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
@ApiOperation(value = "Category API")
public class CategoryController {

    private final CategoryService categoryService;
    private final NoteService noteService;

    @Operation(summary = "카테고리 전체 조회", description = "회원 Id로 카테고리를 조회합니다.")
    @GetMapping("/{id}")
    public List<CategoryDTO> findCategory(@Parameter(description = "회원 Id", in = ParameterIn.PATH) @PathVariable("id") Long userId){
        return convertToDTO(categoryService.findCategoryByUserId(userId));
    }

    @Operation(summary = "카테고리에 해당되는 노트 조회", description = "회원 Id와 카테고리 Id로 노트를 조회합니다.")
    @GetMapping("/{category_id}/user/{id}")
    public Slice<NoteDTO> findNoteByCategory(@Parameter(description = "카테고리 Id", in = ParameterIn.PATH) @PathVariable("category_id") Long categoryId,
                                             @Parameter(description = "회원 Id", in = ParameterIn.PATH) @PathVariable("id") Long userId, Pageable pageable){
        return noteService.findNoteByCategory(userId, categoryId, pageable);
    }

    @Operation(summary = "카테고리 추가", description = "신규 카테고리 추가합니다.")
    @PostMapping
    public List<CategoryDTO> addCategory(@RequestBody CategoryDTO categoryDTO){
        return convertToDTO(categoryService.addCategory(categoryDTO));
    }

    @Operation(summary = "카테고리 업데이트", description = "카테고리 순서, 제목을 업데이트합니다.")
    @PatchMapping
    public List<CategoryDTO> updateCategory(@RequestBody @Valid CategoryDTO categoryDTO){
        return convertToDTO(categoryService.updateCategory(categoryDTO));
    }

    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
    @DeleteMapping
    public List<CategoryDTO> deleteCategory(@RequestBody @Valid CategoryDTO categoryDTO){
        return convertToDTO(categoryService.deleteCategory(categoryDTO.getUserId(), categoryDTO.getCategoryId()));
    }

    /**
     * category -> categoryDTO로 변환
     */
    private List<CategoryDTO> convertToDTO(List<Category> categories){
        return categories.stream()
                    .map(category -> new CategoryDTO(category.getId(), category.getName(), category.getOrder(), category.getUser().getId()))
                    .collect(Collectors.toList());

    }
}
