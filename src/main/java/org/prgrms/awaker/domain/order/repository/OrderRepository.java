package org.prgrms.awaker.domain.order.repository;

import org.prgrms.awaker.domain.order.Order;
import org.prgrms.awaker.domain.order.dto.OrderFilterDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order insert(Order order);

    Optional<Order> findById(UUID orderId);

    List<Order> findAll(OrderSortMethod sortMethod);

    List<Order> findByFilter(OrderFilterDto filter, OrderSortMethod sortMethod);

    Order update(Order order);

    void deleteAll();

}
