package org.prgrms.awaker.domain.product.dto;

import org.prgrms.awaker.domain.product.Product;
import org.prgrms.awaker.domain.product.ProductValidator;
import org.prgrms.awaker.domain.product.category.Category;
import org.prgrms.awaker.global.enums.Target;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResDto(UUID productId, String productName,
                            Target targetUser, String description,
                            Category category, long price,
                            long discountedPrice, LocalDateTime createdAt) {

    public ProductResDto{
        ProductValidator.validateDiscountedPrice(discountedPrice, price);
        ProductValidator.validateProductName(productName);
        ProductValidator.validatePrice(price);
        ProductValidator.validateDescription(description);
    }

    public static ProductResDto of(Product product) {
        return new ProductResDto(
                product.getProductId(),
                product.getProductName(),
                product.getTargetUser(),
                product.getDescription(),
                product.getCategory(),
                product.getPrice(),
                product.getDiscountedPrice(),
                product.getCreatedAt()
        );
    }
}
