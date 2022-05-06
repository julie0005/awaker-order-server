package org.prgrms.awaker.domain.product;

import lombok.Getter;
import org.prgrms.awaker.global.Utils;
import org.prgrms.awaker.global.enums.Target;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Product {
    private final UUID productId;
    private String productName;
    private Target targetUser;
    private String description;
    private final UUID categoryId;
    private long price;
    private long discountedPrice;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product(UUID productId, UUID categoryId, String productName, long price, Target targetUser){
        ProductValidator.validateProductName(productName);
        ProductValidator.validatePrice(price);

        this.productId = productId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.price = price;
        this.discountedPrice = price;
        this.targetUser = targetUser;
        this.createdAt = Utils.now();
        this.updatedAt = Utils.now();
    }

    public Product(UUID productId, UUID categoryId, String productName, long price, Target targetUser, String description){
        ProductValidator.validateProductName(productName);
        ProductValidator.validatePrice(price);
        ProductValidator.validateDescription(description);

        this.productId = productId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.price = price;
        this.discountedPrice = price;
        this.description = description;
        this.targetUser = targetUser;
        this.createdAt = Utils.now();
        this.updatedAt = Utils.now();
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
}
