package dev.sprohar.eshopinventoryservice.service;

import java.util.List;

import dev.sprohar.eshopinventoryservice.error.ItemNotFoundException;
import dev.sprohar.eshopinventoryservice.model.Inventory;
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
    public List<InventoryQueryResponseDto> isInStock(Long[] skus) throws ItemNotFoundException {
        List<Inventory> list = inventoryRepository.findBySkuIn(skus);
        if (list.isEmpty()) {
            throw new ItemNotFoundException("Not all items were found");
        }

        return list.stream()
                .map(inventory -> InventoryQueryResponseDto.builder()
                        .sku(inventory.getSku())
                        .isInStock(inventory.getQuantity() > 0)
                        .build())
                .toList();
    }
}
