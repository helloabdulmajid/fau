package in.abdulmajid.fau.category.controller;

import in.abdulmajid.fau.category.dto.CategoryResponse;
import in.abdulmajid.fau.category.dto.CreateCategoryRequest;
import in.abdulmajid.fau.category.service.CategoryService;

import in.abdulmajid.fau.common.ApiResponse;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    // =========================================================
    // SERVICES
    // =========================================================

    private final CategoryService categoryService;

    // =========================================================
    // CREATE NEW CATEGORY
    // =========================================================

    @PostMapping
    public ApiResponse<CategoryResponse> createCategory(
            @Valid @RequestBody CreateCategoryRequest request
    ) {

        return ApiResponse.<CategoryResponse>builder()
                .success(true)
                .message("Category created successfully")
                .data(
                        categoryService.createCategory(request)
                )
                .build();
    }

    // =========================================================
    // GET ALL CATEGORIES
    // =========================================================

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAllCategories() {

        return ApiResponse.<List<CategoryResponse>>builder()
                .success(true)
                .message("Categories fetched successfully")
                .data(
                        categoryService.getAllCategories()
                )
                .build();
    }

    // =========================================================
    // GET SINGLE CATEGORY BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getCategoryById(
            @PathVariable Long id
    ) {

        return ApiResponse.<CategoryResponse>builder()
                .success(true)
                .message("Category fetched successfully")
                .data(
                        categoryService.getCategoryById(id)
                )
                .build();
    }

    // =========================================================
    // UPDATE EXISTING CATEGORY
    // =========================================================

    @PutMapping("/{id}")
    public ApiResponse<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CreateCategoryRequest request
    ) {

        return ApiResponse.<CategoryResponse>builder()
                .success(true)
                .message("Category updated successfully")
                .data(
                        categoryService.updateCategory(id, request)
                )
                .build();
    }

    // =========================================================
    // DELETE CATEGORY
    // =========================================================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(
            @PathVariable Long id
    ) {

        categoryService.deleteCategory(id);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Category deleted successfully")
                .build();
    }
}