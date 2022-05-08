package org.prgrms.awaker.domain.order.service;

import org.prgrms.awaker.domain.order.Order;
import org.prgrms.awaker.domain.order.dto.NewOrderReqDto;
import org.prgrms.awaker.domain.order.dto.OrderFilterDto;
import org.prgrms.awaker.domain.order.repository.OrderSortMethod;
import org.prgrms.awaker.global.enums.OrderStatus;

import java.util.Optional;
import java.util.UUID;

public interface OrderService {
    Order createOrder(UUID userId, NewOrderReqDto newOrderReqDto);

    Order updateStatus(UUID userId, UUID orderId, OrderStatus orderStatus);

    Order updateAddress(UUID userId, UUID orderId, String address);

    Optional<Order> getAllOrders(UUID userId, OrderFilterDto filterDto, OrderSortMethod sortMethod);
}
