package dev.sprohar.eshopinventoryservice.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.sprohar.eshopinventoryservice.dto.InventoryQueryResponseDto;
import dev.sprohar.eshopinventoryservice.service.InventoryService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(final InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * Checks if the product(s) is/are in stock.
     * 
     * @param sku A single SKU or a comma-separated list of SKUs.
     * @return true if the product(s) is/are in stock, false otherwise.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryQueryResponseDto> isInStock(@RequestParam String sku) {
        Long[] skus = Arrays.stream(sku.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .toArray(Long[]::new);

        return inventoryService.isInStock(skus);
    }
}
