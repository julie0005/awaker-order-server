package org.prgrms.awaker.domain.order;

import lombok.Builder;
import lombok.Getter;
import org.prgrms.awaker.global.Utils;
import org.prgrms.awaker.global.enums.OrderStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class Order {
    @NotNull
    private final UUID orderId;
    @NotNull
    private final UUID userId;
    private final long totalPrice;
    private final long totalDiscount;
    @NotEmpty
    private final List<OrderItem> orderItems;
    @NotNull
    private String address;
    @NotNull
    private String postcode;
    @NotNull
    private OrderStatus orderStatus;
    @NotNull
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public Order(UUID orderId, UUID userId, long totalPrice, long totalDiscount, List<OrderItem> orderItems, String address, String postcode) {
        OrderValidator.validateTotalPrice(totalPrice);
        OrderValidator.validateTotalDiscount(totalDiscount);
        OrderValidator.validateAddress(address);
        OrderValidator.validatePostcode(postcode);

        this.orderItems = orderItems;
        this.orderId = orderId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.totalDiscount = totalDiscount;
        this.address = address;
        this.postcode = postcode;
        this.orderStatus = OrderStatus.ACCEPTED;
        this.createdAt = Utils.now();
        this.updatedAt = Utils.now();
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        this.updatedAt = Utils.now();
    }

    public void setAddress(String address) {
        OrderValidator.validateAddress(address);
        this.address = address;
        this.updatedAt = Utils.now();
    }

    public void setPostcode(String postcode) {
        OrderValidator.validatePostcode(postcode);
        this.postcode = postcode;
        this.updatedAt = Utils.now();
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
