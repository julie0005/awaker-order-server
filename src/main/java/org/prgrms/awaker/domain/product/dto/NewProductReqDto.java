package org.prgrms.awaker.domain.product.dto;

import org.prgrms.awaker.domain.product.ProductValidator;
import org.prgrms.awaker.domain.product.category.Category;
import org.prgrms.awaker.global.enums.Target;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public record NewProductReqDto(@NotBlank String productName,
                               @NotNull Target targetUser,
                               String description,
                               @NotNull UUID categoryId,
                               long price,
                               long discountedPrice) {
    public NewProductReqDto {
        ProductValidator.validateDescription(description);
        ProductValidator.validatePrice(price);
        ProductValidator.validateDescription(description);
        ProductValidator.validateDiscountedPrice(discountedPrice, price);
    }
}
