package com.primebasket.inventory_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InventoryUpdateResponseDto {
    private Long inventoryId;

    private Long productId;

    private Integer quantity;

    private Integer reservedQuantity;

    private LocalDateTime updatedAt;
}
