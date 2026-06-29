package in.abdulmajid.fau.category.service;

import in.abdulmajid.fau.category.dto.CategoryResponse;
import in.abdulmajid.fau.category.dto.CreateCategoryRequest;
import in.abdulmajid.fau.category.entity.Category;
import in.abdulmajid.fau.category.repository.CategoryRepository;

import in.abdulmajid.fau.exception.DuplicateResourceException;
import in.abdulmajid.fau.exception.ResourceNotFoundException;

import in.abdulmajid.fau.offer.repository.OfferRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    // =========================================================
    // REPOSITORIES
    // =========================================================

    private final CategoryRepository categoryRepository;

    private final OfferRepository offerRepository;

    // =========================================================
    // CREATE CATEGORY
    // =========================================================

    public CategoryResponse createCategory(
            CreateCategoryRequest request
    ) {

        // -----------------------------------------------------
        // CHECK DUPLICATE NAME
        // -----------------------------------------------------

        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {

            throw new DuplicateResourceException(
                    "Category already exists"
            );
        }

        // -----------------------------------------------------
        // CHECK DUPLICATE SLUG
        // -----------------------------------------------------

        if (categoryRepository.existsBySlug(request.getSlug())) {

            throw new DuplicateResourceException(
                    "Category slug already exists"
            );
        }

        // -----------------------------------------------------
        // CREATE CATEGORY OBJECT
        // -----------------------------------------------------

        Category category = new Category();

        category.setName(request.getName());

        category.setSlug(request.getSlug());

        category.setIcon(request.getIcon());

        category.setDescription(request.getDescription());

        category.setActive(
                request.getActive() != null
                        ? request.getActive()
                        : true
        );

        // -----------------------------------------------------
        // SAVE CATEGORY
        // -----------------------------------------------------

        Category savedCategory = categoryRepository.save(category);

        // -----------------------------------------------------
        // RETURN RESPONSE
        // -----------------------------------------------------

        return mapToResponse(savedCategory);
    }

    // =========================================================
    // GET ALL CATEGORIES
    // =========================================================

    public List<CategoryResponse> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================================================
    // GET CATEGORY BY ID
    // =========================================================

    public CategoryResponse getCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found"
                        )
                );

        return mapToResponse(category);
    }

    // =========================================================
    // UPDATE CATEGORY
    // =========================================================

    public CategoryResponse updateCategory(
            Long id,
            CreateCategoryRequest request
    ) {

        // -----------------------------------------------------
        // FIND EXISTING CATEGORY
        // -----------------------------------------------------

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found"
                        )
                );

        // -----------------------------------------------------
        // UPDATE CATEGORY DATA
        // -----------------------------------------------------

        category.setName(request.getName());

        category.setSlug(request.getSlug());

        category.setIcon(request.getIcon());

        category.setDescription(request.getDescription());

        if (request.getActive() != null) {
            category.setActive(request.getActive());
        }

        // -----------------------------------------------------
        // SAVE UPDATED CATEGORY
        // -----------------------------------------------------

        Category updatedCategory = categoryRepository.save(category);

        // -----------------------------------------------------
        // RETURN RESPONSE
        // -----------------------------------------------------

        return mapToResponse(updatedCategory);
    }

    // =========================================================
    // DELETE CATEGORY
    // =========================================================

    public void deleteCategory(Long id) {

        // -----------------------------------------------------
        // FIND CATEGORY
        // -----------------------------------------------------

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found"
                        )
                );

        // -----------------------------------------------------
        // CHECK ASSOCIATED OFFERS
        // -----------------------------------------------------

        if (offerRepository.existsByCategory_Id(id)) {

            throw new DuplicateResourceException(
                    "Cannot delete category because offers are associated with it"
            );
        }

        // -----------------------------------------------------
        // DELETE CATEGORY
        // -----------------------------------------------------

        categoryRepository.delete(category);
    }

    // =========================================================
    // MAP ENTITY TO RESPONSE DTO
    // =========================================================

    private CategoryResponse mapToResponse(
            Category category
    ) {

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .icon(category.getIcon())
                .description(category.getDescription())
                .active(category.getActive())
                .build();
    }
}