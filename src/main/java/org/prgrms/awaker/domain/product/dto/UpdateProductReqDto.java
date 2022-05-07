package org.prgrms.awaker.domain.product.dto;

import org.prgrms.awaker.domain.product.ProductValidator;
import org.prgrms.awaker.domain.product.category.Category;
import org.prgrms.awaker.global.enums.Target;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public record UpdateProductReqDto(@NotNull UUID productId,
                                  @NotBlank String productName,
                                  @NotNull Target targetUser,
                                  String description,
                                  @NotNull UUID categoryId,
                                  long price,
                                  long discountedPrice) {
    public UpdateProductReqDto(UUID productId, String productName, Target targetUser, String description, UUID categoryId, long price, long discountedPrice) {
        ProductValidator.validateDescription(description);
        ProductValidator.validatePrice(price);
        ProductValidator.validateDescription(description);
        ProductValidator.validateDiscountedPrice(discountedPrice, price);

        this.productId = productId;
        this.productName = productName;
        this.targetUser = targetUser;
        this.description = description;
        this.categoryId = categoryId;
        this.price = price;
        this.discountedPrice = discountedPrice;
    }
}
