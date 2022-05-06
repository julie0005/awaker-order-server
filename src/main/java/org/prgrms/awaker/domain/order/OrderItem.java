package org.prgrms.awaker.domain.order;

import lombok.Getter;
import org.prgrms.awaker.global.Utils;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class OrderItem {
    private final UUID itemId;
    private final UUID productId;
    private long productTotalPrice;
    private long productTotalDiscount;
    private int quantity;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrderItem(UUID itemId, UUID productId, long productTotalPrice, long productTotalDiscount, int quantity, LocalDateTime createdAt) {
        OrderItemValidator.validatePrice(productTotalPrice, productTotalDiscount);
        OrderItemValidator.validateQuantity(quantity);

        this.itemId = itemId;
        this.productId = productId;
        this.productTotalPrice = productTotalPrice;
        this.productTotalDiscount = productTotalDiscount;
        this.quantity = quantity;
        this.createdAt = Utils.now();
    }

    public void setProductTotalPrice(long productTotalPrice) {
        OrderItemValidator.validatePrice(productTotalPrice, productTotalDiscount);
        this.productTotalPrice = productTotalPrice;
        this.updatedAt = Utils.now();
    }

    public void setProductTotalDiscount(long productTotalDiscount) {
        OrderItemValidator.validatePrice(productTotalPrice, productTotalDiscount);
        this.productTotalDiscount = productTotalDiscount;
        this.updatedAt = Utils.now();
    }

    public void setQuantity(int quantity) {
        OrderItemValidator.validateQuantity(quantity);
        this.quantity = quantity;
        this.updatedAt = Utils.now();
    }
}
