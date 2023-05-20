package dev.sprohar.eshoporderservice.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderItemDto {
    private String sku;
    private Integer quantity;
    private BigDecimal price;
}
