package org.prgrms.awaker.domain.product.category;

import lombok.Getter;
import org.prgrms.awaker.global.Utils;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
// TODO : 생성시간 리팩토링
// Product 입장에서는 VO, Category 자체의 입장으로는 entity
public class Category {
    private final UUID categoryId;
    private String categoryName;
    private UUID parentId;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Category(UUID categoryId, String categoryName, UUID parentId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        CategoryValidator.validateCategoryName(categoryName);
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.parentId = parentId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Category(UUID categoryId, String categoryName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        CategoryValidator.validateCategoryName(categoryName);
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void setCategoryName(String categoryName) {
        CategoryValidator.validateCategoryName(categoryName);
        this.categoryName = categoryName;
        this.updatedAt = Utils.now();
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return categoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return categoryId.equals(category.categoryId) && categoryName.equals(category.categoryName) && parentId.equals(category.parentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, categoryName, parentId);
    }
}
