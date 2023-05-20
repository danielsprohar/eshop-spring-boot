package dev.sprohar.eshoporderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryQueryResponseDto {
    private String sku;
    private boolean isInStock;
}
