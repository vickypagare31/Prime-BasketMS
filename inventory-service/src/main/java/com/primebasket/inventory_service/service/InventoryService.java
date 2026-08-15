package com.primebasket.inventory_service.service;

import com.primebasket.inventory_service.dto.InventoryRequestDto;
import com.primebasket.inventory_service.dto.InventoryResponseDto;

public interface InventoryService {

    InventoryResponseDto addInventory(InventoryRequestDto requestDto);

    InventoryResponseDto getInventory(Long productId);

    InventoryResponseDto updateStock(Long productId, InventoryRequestDto requestDto);

    InventoryResponseDto deductStock(Long productId, InventoryRequestDto requestDto);

    InventoryResponseDto reserveStock(Long productId, InventoryRequestDto requestDto);

    InventoryResponseDto releaseReservedStock(Long productId, InventoryRequestDto requestDto);
}
