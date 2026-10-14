package com.primebasket.inventory_service.service;

import com.primebasket.inventory_service.dto.InventoryRequestDto;
import com.primebasket.inventory_service.dto.InventoryResponseDto;
import com.primebasket.inventory_service.dto.InventoryUpdateResponseDto;
import com.primebasket.inventory_service.dto.StockQuantityRequestDto;

public interface InventoryService {

    InventoryResponseDto addInventory(InventoryRequestDto requestDto);

    InventoryResponseDto getInventory(Long productId);

    InventoryUpdateResponseDto updateStock(Long productId, StockQuantityRequestDto requestDto);

    InventoryUpdateResponseDto deductStock(Long productId, StockQuantityRequestDto requestDto);

    InventoryUpdateResponseDto reserveStock(Long productId, StockQuantityRequestDto requestDto);

    InventoryUpdateResponseDto releaseReservedStock(Long productId, StockQuantityRequestDto requestDto);

    InventoryUpdateResponseDto confirmReservedStock(Long productId, StockQuantityRequestDto requestDto);

}
