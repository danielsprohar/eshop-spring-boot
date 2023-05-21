package dev.sprohar.eshopinventoryservice.controller;

import java.util.Arrays;
import java.util.List;

import dev.sprohar.eshopinventoryservice.error.ItemNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.sprohar.eshopinventoryservice.dto.InventoryQueryResponseDto;
import dev.sprohar.eshopinventoryservice.service.InventoryService;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(final InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryQueryResponseDto> isInStock(@RequestParam String sku) {
        Long[] skus = Arrays.stream(sku.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .toArray(Long[]::new);

        try {
            return inventoryService.isInStock(skus);
        } catch (ItemNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
