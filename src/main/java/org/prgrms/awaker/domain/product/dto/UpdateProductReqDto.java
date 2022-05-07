package org.prgrms.awaker.domain.product.dto;

import org.prgrms.awaker.domain.product.category.Category;
import org.prgrms.awaker.global.enums.Target;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public record UpdateProductReqDto(@NotNull UUID productId,
                                  @NotBlank String productName,
                                  @NotNull Target targetUser,
                                  String description,
                                  @NotNull Category category,
                                  long price,
                                  long discountedPrice) {
    public UpdateProductReqDto(UUID productId, String productName, Target targetUser, String description, Category category, long price, long discountedPrice) {
        this.productId = productId;
        this.productName = productName;
        this.targetUser = targetUser;
        this.description = description;
        this.category = category;
        this.price = price;
        this.discountedPrice = discountedPrice;
    }
}
