package org.prgrms.awaker.domain.cart;

import lombok.Getter;
import org.prgrms.awaker.domain.product.Product;
import org.prgrms.awaker.global.Utils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class Cart {
    private final UUID cartId;
    private final UUID userId;
    private final List<Product> productList;
    private final LocalDateTime createdAt;

    public Cart(UUID cartId, UUID userId, List<Product> productList) {
        this.cartId = cartId;
        this.userId = userId;
        this.productList = productList;
        this.createdAt = Utils.now();
    }
}
