package org.prgrms.awaker.domain.order.dto;

import org.prgrms.awaker.domain.order.OrderItem;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record NewOrderReqDto(@NotNull UUID userId,
                             @NotEmpty List<OrderItem> orderItems,
                             @NotNull String address, @NotNull String postcode) {
}
