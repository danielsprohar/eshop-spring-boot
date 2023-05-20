package dev.sprohar.eshoporderservice.service;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import dev.sprohar.eshoporderservice.dto.CreateOrderDto;
import dev.sprohar.eshoporderservice.dto.CreateOrderItemDto;
import dev.sprohar.eshoporderservice.dto.InventoryQueryResponseDto;
import dev.sprohar.eshoporderservice.enums.OrderStatus;
import dev.sprohar.eshoporderservice.error.ItemsUnavailableException;
import dev.sprohar.eshoporderservice.model.Order;
import dev.sprohar.eshoporderservice.model.OrderItem;
import dev.sprohar.eshoporderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public OrderService(
            final OrderRepository orderRepository,
            final WebClient webClient) {
        this.orderRepository = orderRepository;
        this.webClient = webClient;
    }

    private OrderItem mapToOrderItem(CreateOrderItemDto dto) {
        return OrderItem.builder()
                .sku(dto.getSku())
                .quantity(dto.getQuantity())
                .price(dto.getPrice())
                .build();
    }

    private Order mapToOrder(CreateOrderDto dto) {
        return Order.builder()
                .customerId(dto.getCustomerId())
                .status(dto.getStatus() == null ? OrderStatus.CREATED.toString() : dto.getStatus())
                .items(dto.getItems().stream().map(this::mapToOrderItem).toList())
                .createdAt(OffsetDateTime.now())
                .build();
    }

    public void create(CreateOrderDto createOrderDto) throws ItemsUnavailableException {
        Order order = mapToOrder(createOrderDto);
        List<String> skus = order.getItems().stream().map(OrderItem::getSku).toList();
        String query = String.format("?sku=%s", String.join(",", skus));

        log.info("Querying inventory service for stock of items: {}", skus);

        InventoryQueryResponseDto[] response = webClient.get()
                .uri("http://localhost:8082/api/inventory" + query)
                .retrieve()
                .bodyToMono(InventoryQueryResponseDto[].class)
                .block();

        log.info("Received response from inventory service: {}", (Object[]) response);

        List<InventoryQueryResponseDto> outOfStockItems = Arrays.stream(response)
                .filter(item -> !item.isInStock())
                .toList();

        if (!outOfStockItems.isEmpty()) {
            String outOfStockSkus = outOfStockItems.stream()
                    .map(InventoryQueryResponseDto::getSku)
                    .toList()
                    .toString();

            throw new ItemsUnavailableException("The following items are out of stock: " + outOfStockSkus);
        }

        orderRepository.save(order);
    }
}
