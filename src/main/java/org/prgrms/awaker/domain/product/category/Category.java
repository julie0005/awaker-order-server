package org.prgrms.awaker.domain.product.category;

import lombok.Getter;
import org.prgrms.awaker.global.Utils;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Category {
    private final UUID categoryId;
    private String categoryName;
    private final UUID parentId;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Category(UUID categoryId, String categoryName, UUID parentId) {
        this.categoryId = categoryId;
        CategoryValidator.validateCategoryName(categoryName);
        this.categoryName = categoryName;
        this.parentId = parentId;
        this.createdAt = Utils.now();
        this.updatedAt = Utils.now();
    }

    public void setCategoryName(String categoryName) {
        CategoryValidator.validateCategoryName(categoryName);
        this.categoryName = categoryName;
        this.updatedAt = Utils.now();
    }
}
