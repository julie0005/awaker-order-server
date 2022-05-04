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

    // entity에 있어도 되고, 없어도 되는 필드가 많으면 생성자를 어떻게 생성해야 하나요? - setter 이용?

    public Product(UUID productId, UUID categoryId, String productName, long price, Target targetUser){
        this.productId = productId;
        this.categoryId = categoryId;

        ProductValidator.validateProductName(productName);
        this.productName = productName;

        ProductValidator.validatePrice(price);
        this.price = price;
        this.discountedPrice = price;

        this.targetUser = targetUser;
        createdAt = Utils.now();
        updatedAt = Utils.now();
    }

    public Product(UUID productId, UUID categoryId, String productName, long price, Target targetUser, String description){
        this.productId = productId;
        this.categoryId = categoryId;

        ProductValidator.validateProductName(productName);
        this.productName = productName;

        ProductValidator.validatePrice(price);
        this.price = price;
        this.discountedPrice = price;

        ProductValidator.validateDescription(description);
        this.description = description;
        this.targetUser = targetUser;
        createdAt = Utils.now();
        updatedAt = Utils.now();
    }

    public void setProductName(String productName) {
        ProductValidator.validateProductName(productName);
        this.productName = productName;
    }

    public void setDescription(String description) {
        ProductValidator.validateDescription(description);
        this.description = description;
    }

    public void setPrice(long price) {
        ProductValidator.validatePrice(price);
        this.price = price;
    }

    public void setDiscountedPrice(long discountedPrice) {
        ProductValidator.validateDiscountedPrice(discountedPrice, this.price);
        this.discountedPrice = discountedPrice;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
