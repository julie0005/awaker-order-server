package org.prgrms.awaker.domain.product.dto;

import lombok.Builder;
import org.prgrms.awaker.domain.product.Product;
import org.prgrms.awaker.domain.product.ProductValidator;
import org.prgrms.awaker.domain.product.category.dto.CategoryResDto;
import org.prgrms.awaker.global.enums.Target;

import java.time.LocalDateTime;
import java.util.UUID;


public record ProductResDto(UUID productId, String productName,
                            Target targetUser, String description,
                            CategoryResDto category, long price,
                            long discountedPrice, LocalDateTime createdAt) {
    @Builder
    public ProductResDto{
        ProductValidator.validateDiscountedPrice(discountedPrice, price);
        ProductValidator.validateProductName(productName);
        ProductValidator.validatePrice(price);
        ProductValidator.validateDescription(description);
    }

    public static ProductResDto of(Product product) {
        return ProductResDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .targetUser(product.getTargetUser())
                .description(product.getDescription())
                .category(CategoryResDto.of(product.getCategory()))
                .price(product.getPrice())
                .discountedPrice(product.getDiscountedPrice())
                .createdAt(product.getCreatedAt())
                .build();
    }
}
