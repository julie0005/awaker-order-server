package org.prgrms.awaker.domain.product.category.dto;

import org.prgrms.awaker.domain.product.category.Category;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public record RootCategoryResDto(@NotNull UUID categoryId, @NotBlank String categoryName) {

    public static RootCategoryResDto of(Category category) {
        return new RootCategoryResDto(category.getCategoryId(), category.getCategoryName());
    }
}
