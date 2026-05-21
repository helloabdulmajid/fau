package in.abdulmajid.cardiq.category.controller;

import in.abdulmajid.cardiq.category.dto.CategoryResponse;
import in.abdulmajid.cardiq.category.dto.CreateCategoryRequest;
import in.abdulmajid.cardiq.category.service.CategoryService;
import in.abdulmajid.cardiq.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ApiResponse<CategoryResponse> createCategory(
            @Valid @RequestBody CreateCategoryRequest request
    ) {

        return ApiResponse.<CategoryResponse>builder()
                .success(true)
                .message("Category created successfully")
                .data(categoryService.createCategory(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAllCategories() {

        return ApiResponse.<List<CategoryResponse>>builder()
                .success(true)
                .message("Categories fetched successfully")
                .data(categoryService.getAllCategories())
                .build();
    }
}