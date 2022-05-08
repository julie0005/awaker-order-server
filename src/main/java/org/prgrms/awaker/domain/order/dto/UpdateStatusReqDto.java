package org.prgrms.awaker.domain.order.dto;

import org.prgrms.awaker.global.enums.OrderStatus;

import java.util.UUID;

public record UpdateStatusReqDto(UUID orderId, OrderStatus status) {
}

