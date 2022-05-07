package org.prgrms.awaker.domain.product;

import lombok.Builder;
import lombok.Getter;
import org.prgrms.awaker.domain.product.category.Category;
import org.prgrms.awaker.global.Utils;
import org.prgrms.awaker.global.enums.Target;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Product {
    @NotNull
    private final UUID productId;
    @NotBlank
    private String productName;
    @NotNull
    private Target targetUser;
    private String description;
    @NotNull
    private Category category;
    private long price;
    private long discountedPrice;
    @NotNull
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public Product(UUID productId, Category category, String productName, long price, long discountedPrice, Target targetUser, String description, LocalDateTime createdAt, LocalDateTime updatedAt){
        ProductValidator.validateProductName(productName);
        ProductValidator.validatePrice(price);
        ProductValidator.validateDescription(description);
        ProductValidator.validateDiscountedPrice(discountedPrice, price);

        this.productId = productId;
        this.category = category;
        this.productName = productName;
        this.price = price;
        this.discountedPrice = discountedPrice;
        this.description = description;
        this.targetUser = targetUser;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void setProductName(String productName) {
        ProductValidator.validateProductName(productName);
        this.productName = productName;
        this.updatedAt = Utils.now();
    }

    public void setDescription(String description) {
        ProductValidator.validateDescription(description);
        this.description = description;
        this.updatedAt = Utils.now();
    }

    public void setPrice(long price) {
        ProductValidator.validatePrice(price);
        this.price = price;
        this.updatedAt = Utils.now();
    }

    public void setTargetUser(Target targetUser) {
        this.targetUser = targetUser;
    }

    public void setDiscountedPrice(long discountedPrice) {
        ProductValidator.validateDiscountedPrice(discountedPrice, this.price);
        this.discountedPrice = discountedPrice;
        this.updatedAt = Utils.now();
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
