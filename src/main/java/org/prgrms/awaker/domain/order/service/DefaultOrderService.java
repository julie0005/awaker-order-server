package org.prgrms.awaker.domain.order.service;

import org.prgrms.awaker.domain.order.Order;
import org.prgrms.awaker.domain.order.OrderItem;
import org.prgrms.awaker.domain.order.dto.NewOrderReqDto;
import org.prgrms.awaker.domain.order.dto.OrderFilterDto;
import org.prgrms.awaker.domain.order.repository.OrderRepository;
import org.prgrms.awaker.domain.order.repository.OrderSortMethod;
import org.prgrms.awaker.domain.user.User;
import org.prgrms.awaker.domain.user.UserRepository;
import org.prgrms.awaker.global.enums.OrderStatus;
import org.prgrms.awaker.global.exception.UnknownException;

import java.util.Optional;
import java.util.UUID;

public class DefaultOrderService implements OrderService{

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public DefaultOrderService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Order createOrder(UUID userId, NewOrderReqDto newOrderReqDto) {
        long totalPrice = newOrderReqDto.orderItems().stream().mapToLong(OrderItem::getProductTotalPrice).sum();
        long totalDiscount = newOrderReqDto.orderItems().stream().mapToLong(OrderItem::getProductTotalDiscount).sum();
        User orderer = userRepository.findById(newOrderReqDto.userId()).orElseThrow(()->new UnknownException("존재하지 않는 사용자입니다."));
        Order newOrder = Order.builder()
                .orderId(UUID.randomUUID())
                .orderItems(newOrderReqDto.orderItems())
                .user(orderer)
                .address(newOrderReqDto.address())
                .postcode(newOrderReqDto.postcode())
                .totalPrice(totalPrice)
                .totalDiscount(totalDiscount)
                .build();
        return orderRepository.insert(newOrder);
    }

    @Override
    public Order updateStatus(UUID userId, UUID orderId, OrderStatus orderStatus) {
        User orderer = userRepository.findById(userId).orElseThrow(()->new UnknownException("존재하지 않는 사용자입니다."));
        Order order = orderRepository.findById(orderId).orElseThrow(()->new UnknownException("존재하지 않는 주문입니다."));
        order.setOrderStatus(orderStatus);
        return orderRepository.update(order);
    }

    @Override
    public Order updateAddress(UUID userId, UUID orderId, String address) {
        User orderer = userRepository.findById(userId).orElseThrow(()->new UnknownException("존재하지 않는 사용자입니다."));
        Order order = orderRepository.findById(orderId).orElseThrow(()->new UnknownException("존재하지 않는 주문입니다."));
        order.setAddress(address);
        return orderRepository.update(order);
    }

    @Override
    public Optional<Order> getAllOrders(UUID userId, OrderFilterDto filterDto, OrderSortMethod sortMethod) {
        User orderer = userRepository.findById(userId).orElseThrow(()->new UnknownException("존재하지 않는 사용자입니다."));
        orderRepository.findByFilter(filterDto, sortMethod);
        return Optional.empty();
    }
}
