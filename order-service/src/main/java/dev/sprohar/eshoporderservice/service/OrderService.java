package dev.sprohar.eshoporderservice.service;

import org.springframework.stereotype.Service;

import dev.sprohar.eshoporderservice.dto.CreateOrderDto;
import dev.sprohar.eshoporderservice.dto.CreateOrderItemDto;
import dev.sprohar.eshoporderservice.model.Order;
import dev.sprohar.eshoporderservice.model.OrderItem;
import dev.sprohar.eshoporderservice.repository.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    private OrderItem mapToOrderItem(CreateOrderItemDto dto) {
        return OrderItem.builder()
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .price(dto.getPrice())
                .build();
    }

    private Order mapToOrder(CreateOrderDto dto) {
        return Order.builder()
                .customerId(dto.getCustomerId())
                .status(dto.getStatus())
                .items(dto.getItems().stream().map(this::mapToOrderItem).toList())
                .build();
    }

    // private OrderItemDto mapToOrderItemDto(OrderItem orderItem) {
    // return OrderItemDto.builder()
    // .productId(orderItem.getProductId())
    // .quantity(orderItem.getQuantity())
    // .price(orderItem.getPrice())
    // .build();
    // }

    // private OrderDto mapToOrderDto(Order order) {
    // return OrderDto.builder()
    // .id(order.getId())
    // .customerId(order.getCustomerId())
    // .status(order.getStatus())
    // .items(order.getItems().stream().map(this::mapToOrderItemDto).toList())
    // .build();
    // }

    public void create(CreateOrderDto createOrderDto) {
        Order order = mapToOrder(createOrderDto);
        orderRepository.save(order);
    }
}
