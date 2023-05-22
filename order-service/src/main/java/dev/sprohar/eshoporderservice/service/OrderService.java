package dev.sprohar.eshoporderservice.service;

import dev.sprohar.eshoporderservice.dto.CreateOrderDto;
import dev.sprohar.eshoporderservice.dto.CreateOrderItemDto;
import dev.sprohar.eshoporderservice.dto.InventoryQueryResponseDto;
import dev.sprohar.eshoporderservice.enums.OrderStatus;
import dev.sprohar.eshoporderservice.error.ItemsUnavailableException;
import dev.sprohar.eshoporderservice.model.Order;
import dev.sprohar.eshoporderservice.model.OrderItem;
import dev.sprohar.eshoporderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@Transactional
public class OrderService {
    /**
     * The value corresponds with the value for
     * <code>spring.application.name</code> in properties file
     * within the Inventory Service project.
     */
    private static final String INVENTORY_SERVICE = "inventory-service";

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public OrderService(
            final OrderRepository orderRepository,
            final WebClient.Builder webClientBuilder) {
        this.orderRepository = orderRepository;
        this.webClientBuilder = webClientBuilder;
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

    public Order create(CreateOrderDto createOrderDto) throws ItemsUnavailableException {
        Order order = mapToOrder(createOrderDto);
        List<String> skus = order.getItems().stream().map(OrderItem::getSku).toList();
        String query = String.format("?sku=%s", String.join(",", skus));

        log.info("Querying inventory service for stock of items: {}", skus);

        String url = String.format("http://%s/api/inventory", INVENTORY_SERVICE);
        InventoryQueryResponseDto[]  response;

        try {
            response = webClientBuilder.build()
                    .get()
                    .uri(url + query)
                    .retrieve()
                    .bodyToMono(InventoryQueryResponseDto[].class)
                    .block();
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item(s) not found");
        }

        if (response == null) {
            log.info("Response body is null");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

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

        return orderRepository.save(order);
    }
}
