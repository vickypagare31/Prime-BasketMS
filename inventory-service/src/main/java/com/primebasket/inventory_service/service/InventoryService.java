package com.primebasket.inventory_service.service;

import com.primebasket.inventory_service.dto.InventoryRequestDto;
import com.primebasket.inventory_service.dto.InventoryResponseDto;
import com.primebasket.inventory_service.dto.InventoryUpdateResponseDto;

public interface InventoryService {

    InventoryResponseDto addInventory(InventoryRequestDto requestDto);

    InventoryResponseDto getInventory(Long productId);

    InventoryUpdateResponseDto updateStock(Long productId, InventoryRequestDto requestDto);

    InventoryUpdateResponseDto deductStock(Long productId, InventoryRequestDto requestDto);

    InventoryUpdateResponseDto reserveStock(Long productId, InventoryRequestDto requestDto);

    InventoryUpdateResponseDto releaseReservedStock(Long productId, InventoryRequestDto requestDto);

    InventoryUpdateResponseDto confirmReservedStock(Long productId, InventoryRequestDto requestDto);

}
