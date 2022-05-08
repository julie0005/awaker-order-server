package org.prgrms.awaker.domain.order.controller;

import org.prgrms.awaker.domain.order.Order;
import org.prgrms.awaker.domain.order.dto.*;
import org.prgrms.awaker.domain.order.repository.OrderSortMethod;
import org.prgrms.awaker.domain.order.service.OrderService;
import org.prgrms.awaker.global.ResponseFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/search")
    public ResponseEntity<ResponseFormat> getOrders(@RequestBody OrderFilterDto filterDto, @RequestParam(value = "sort") OrderSortMethod orderSortMethod) {
        List<Order> orders = orderService.getAllOrders(filterDto, orderSortMethod);
        List<OrderResDto> orderResDtos = orders.stream().map(OrderResDto::of).toList();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseFormat(true, HttpStatus.OK.value(), "주문 전체 조회 성공", orderResDtos));
    }
    
    @PostMapping
    public ResponseEntity<ResponseFormat> createOrder(@RequestBody NewOrderReqDto orderReqDto) {
        Order newOrder = orderService.createOrder(orderReqDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseFormat(true, HttpStatus.OK.value(), "주문 생성 성공", OrderResDto.of(newOrder)));
    }
    
    @PutMapping("/address")
    public ResponseEntity<ResponseFormat> updateOrderAddress(@RequestBody UpdateAddressReqDto orderReqDto) {
        Order updatedOrder = orderService.updateAddress(orderReqDto.orderId(), orderReqDto.address());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseFormat(true, HttpStatus.OK.value(), "주문 주소 수정 성공", OrderResDto.of(updatedOrder)));
    }

    @PutMapping("/status")
    public ResponseEntity<ResponseFormat> updateOrderStatus(@RequestBody UpdateStatusReqDto orderReqDto) {
        Order updatedOrder = orderService.updateStatus(orderReqDto.orderId(), orderReqDto.status());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseFormat(true, HttpStatus.OK.value(), "주문 상태 수정 성공", OrderResDto.of(updatedOrder)));
    }



}
