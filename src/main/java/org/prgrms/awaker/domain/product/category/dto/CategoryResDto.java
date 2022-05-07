package org.prgrms.awaker.domain.product.category.dto;

import org.prgrms.awaker.domain.product.category.Category;
import org.prgrms.awaker.domain.product.category.CategoryValidator;

import java.util.UUID;

public record CategoryResDto(UUID categoryId, String categoryName, UUID parentId) {
    public CategoryResDto {
        CategoryValidator.validateCategoryName(categoryName);
    }

    public static CategoryResDto of(Category category) {
        return new CategoryResDto(category.getCategoryId(), category.getCategoryName(), category.getParentId());
    }
}
