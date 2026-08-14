package com.primebasket.inventory_service.service;

import com.primebasket.inventory_service.dto.InventoryRequestDto;
import com.primebasket.inventory_service.dto.InventoryResponseDto;

public interface InventoryService {

    InventoryResponseDto addInventory(InventoryRequestDto requestDto);
}
