package dev.sprohar.eshopinventoryservice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.sprohar.eshopinventoryservice.dto.InventoryQueryResponseDto;
import dev.sprohar.eshopinventoryservice.repository.InventoryRepository;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional(readOnly = true)
    public List<InventoryQueryResponseDto> isInStock(Long[] skus) {
        return inventoryRepository.findBySkuIn(skus)
                .stream()
                .map(inventory -> InventoryQueryResponseDto.builder()
                        .sku(inventory.getSku())
                        .isInStock(inventory.getQuantity() > 0)
                        .build())
                .toList();
    }
}
