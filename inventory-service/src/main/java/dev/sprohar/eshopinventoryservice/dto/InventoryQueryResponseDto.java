package dev.sprohar.eshopinventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryQueryResponseDto {
    private Long sku;
    private boolean isInStock;
}
