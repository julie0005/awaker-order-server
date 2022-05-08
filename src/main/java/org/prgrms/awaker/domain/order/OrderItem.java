package org.prgrms.awaker.domain.order;

import lombok.Builder;
import lombok.Getter;
import org.prgrms.awaker.global.Utils;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class OrderItem {
    @NotNull
    private final UUID productId;
    private long productTotalPrice;
    private long productTotalDiscount;
    private int quantity;
    @NotNull

    @Builder
    public OrderItem(UUID productId, long productTotalPrice, long productTotalDiscount, int quantity) {
        OrderItemValidator.validatePrice(productTotalPrice, productTotalDiscount);
        OrderItemValidator.validateQuantity(quantity);

        this.productId = productId;
        this.productTotalPrice = productTotalPrice;
        this.productTotalDiscount = productTotalDiscount;
        this.quantity = quantity;
    }

    public void setProductTotalPrice(long productTotalPrice) {
        OrderItemValidator.validatePrice(productTotalPrice, productTotalDiscount);
        this.productTotalPrice = productTotalPrice;
    }

    public void setProductTotalDiscount(long productTotalDiscount) {
        OrderItemValidator.validatePrice(productTotalPrice, productTotalDiscount);
        this.productTotalDiscount = productTotalDiscount;
    }

    public void setQuantity(int quantity) {
        OrderItemValidator.validateQuantity(quantity);
        this.quantity = quantity;
    }
}
