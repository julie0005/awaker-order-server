package org.prgrms.awaker.domain.order.dto;

import lombok.Builder;
import org.prgrms.awaker.domain.order.Order;
import org.prgrms.awaker.domain.user.User;
import org.prgrms.awaker.global.enums.OrderStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResDto(@NotNull UUID orderId, @NotNull String userName,
                          @NotNull String userEmail, long totalPrice, long totalDiscount,
                          @NotNull String address, @NotNull String postcode,
                          @NotNull OrderStatus orderStatus,
                          @NotNull LocalDateTime createdAt) {
    @Builder
    public OrderResDto{
    }
    public static OrderResDto of(Order order){
        User user = order.getUser();
        return OrderResDto.builder()
                .orderId(order.getOrderId())
                .userName(user.getUserName())
                .userEmail(user.getEmail())
                .totalPrice(order.getTotalPrice())
                .totalDiscount(order.getTotalDiscount())
                .orderStatus(order.getOrderStatus())
                .address(order.getAddress())
                .postcode(order.getPostcode())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
