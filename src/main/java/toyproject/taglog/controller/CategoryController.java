package toyproject.taglog.controller;

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
public class CategoryController {

    private final CategoryService categoryService;
    private final NoteService noteService;

    @GetMapping("/{id}")
    public List<CategoryDTO> findCategory(@PathVariable("id") Long userId){
        return convertToDTO(categoryService.findCategoryByUserId(userId));
    }

    @GetMapping("/{category_id}/user/{id}")
    public Slice<NoteDTO> findNoteByCategory(@PathVariable("category_id") Long categoryId,
                                             @PathVariable("id") Long userId, Pageable pageable){
        return noteService.findNoteByCategory(userId, categoryId, pageable);
    }

    @PostMapping
    public List<CategoryDTO> addCategory(@RequestBody CategoryDTO categoryDTO){
        return convertToDTO(categoryService.addCategory(categoryDTO));
    }

    @PatchMapping
    public List<CategoryDTO> updateCategory(@RequestBody @Valid CategoryDTO categoryDTO){
        return convertToDTO(categoryService.updateCategory(categoryDTO));
    }

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
