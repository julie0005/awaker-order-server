package org.prgrms.awaker.domain.order.repository;

import org.prgrms.awaker.domain.order.Order;
import org.prgrms.awaker.domain.order.dto.OrderFilterDto;
import org.prgrms.awaker.domain.order.dto.OrderResDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order insert(Order order);

    Optional<OrderResDto> findById(UUID orderId);

    List<OrderResDto> findAll(OrderSortMethod sortMethod);

    List<OrderResDto> findByFilter(OrderFilterDto filter, OrderSortMethod sortMethod);

    Order update(Order order);

    void deleteAll();

}
