package toyproject.taglog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import toyproject.taglog.dto.CategoryDTO;
import toyproject.taglog.entity.Category;
import toyproject.taglog.service.CategoryService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/category/{user_id}")
    public List<CategoryDTO> findCategoryByUserId(@PathVariable("user_id") Long userId){
//        return categoryService.findCategoryByUserId(userId).stream()
//                .map(category -> new CategoryDTO(category.getId(), category.getName(), category.getOrder(), userId))
//                .collect(Collectors.toList());
        return convertToDTO(categoryService.findCategoryByUserId(userId));
    }

    @PostMapping("/category")
    public List<CategoryDTO> addCategory(@RequestBody @Valid CategoryDTO categoryDTO){
        return convertToDTO(categoryService.addCategory(categoryDTO));
    }

    @PutMapping("/category")
    public List<CategoryDTO> updateCategory(@RequestBody @Valid CategoryDTO categoryDTO){
        return convertToDTO(categoryService.updateCategory(categoryDTO));
    }

    @DeleteMapping("/category")
    public List<CategoryDTO> deleteCategory(@RequestBody @Valid CategoryDTO categoryDTO){
        return convertToDTO(categoryService.deleteCategory(categoryDTO.getUserId(), categoryDTO.getCategoryId()));
    }

    private List<CategoryDTO> convertToDTO(List<Category> categories){
        return categories.stream()
                    .map(category -> new CategoryDTO(category.getId(), category.getName(), category.getOrder(), category.getUser().getId()))
                    .collect(Collectors.toList());

    }
}
