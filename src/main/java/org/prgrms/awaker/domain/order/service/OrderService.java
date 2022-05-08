package org.prgrms.awaker.domain.order.service;

import org.prgrms.awaker.domain.order.Order;
import org.prgrms.awaker.domain.order.dto.NewOrderReqDto;
import org.prgrms.awaker.domain.order.dto.OrderFilterDto;
import org.prgrms.awaker.domain.order.repository.OrderSortMethod;
import org.prgrms.awaker.global.enums.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {
    Order createOrder(NewOrderReqDto newOrderReqDto);

    Order updateStatus(UUID orderId, OrderStatus orderStatus);

    Order updateAddress(UUID orderId, String address);

    List<Order> getAllOrders(OrderFilterDto filterDto, OrderSortMethod sortMethod);
}
