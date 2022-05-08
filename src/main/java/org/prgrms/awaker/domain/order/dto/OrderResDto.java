package org.prgrms.awaker.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.prgrms.awaker.domain.order.OrderItem;
import org.prgrms.awaker.domain.order.OrderValidator;
import org.prgrms.awaker.global.enums.OrderStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResDto(@NotNull UUID orderId,
                          @NotNull UUID userId,
                          @NotNull String userEmail,
                          @NotNull String userName, long totalPrice, long totalDiscount,
                          @NotEmpty List<OrderItem> orderItems,
                          @NotNull String address, @NotNull String postcode,
                          @NotNull OrderStatus orderStatus,
                          @NotNull LocalDateTime createdAt) {
    @Builder
    public OrderResDto{
        OrderValidator.validatePostcode(postcode);
        OrderValidator.validateAddress(address);
        OrderValidator.validateTotalPrice(totalPrice);
        OrderValidator.validateTotalDiscount(totalDiscount);
    }
}
