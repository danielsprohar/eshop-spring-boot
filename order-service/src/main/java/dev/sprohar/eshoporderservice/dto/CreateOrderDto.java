package dev.sprohar.eshoporderservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderDto {
    private Long customerId;
    private String status;
    List<CreateOrderItemDto> items;
}
