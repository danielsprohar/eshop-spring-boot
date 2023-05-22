package dev.sprohar.eshoporderservice.controller;

import dev.sprohar.eshoporderservice.dto.CreateOrderDto;
import dev.sprohar.eshoporderservice.error.ItemsUnavailableException;
import dev.sprohar.eshoporderservice.model.Order;
import dev.sprohar.eshoporderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    /**
     * The circuit breaker instance name.
     * This value corresponds with the instance name in
     * <code>application.properties</code>:<br><br>
     * <code>
     *     resilience4j.circuitbreaker.instances.inventory
     * </code>
     */
    private static final String CB_INVENTORY_SERVICE = "inventory";
    private final OrderService orderService;


    public OrderController(final OrderService orderService) {
        this.orderService = orderService;
    }

    public CompletableFuture<Order> createOrderFallback(CreateOrderDto dto, RuntimeException e) {
        log.error("Failed to create order: {}", e.getMessage());
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @TimeLimiter(name = CB_INVENTORY_SERVICE)
    @Retry(name = CB_INVENTORY_SERVICE)
    @CircuitBreaker(name = CB_INVENTORY_SERVICE, fallbackMethod = "createOrderFallback")
    public CompletableFuture<Order> create(@RequestBody final CreateOrderDto createOrderDto) {
        try {
            return CompletableFuture.supplyAsync(() -> orderService.create(createOrderDto));
        } catch (ItemsUnavailableException e) {
            log.error("Failed to create order: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
