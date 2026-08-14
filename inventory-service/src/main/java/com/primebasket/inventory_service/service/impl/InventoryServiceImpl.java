package com.primebasket.inventory_service.service.impl;

import com.primebasket.inventory_service.dto.InventoryRequestDto;
import com.primebasket.inventory_service.dto.InventoryResponseDto;
import com.primebasket.inventory_service.entity.Inventory;
import com.primebasket.inventory_service.exception.InventoryAlreadyExistsException;
import com.primebasket.inventory_service.mapper.InventoryMapper;
import com.primebasket.inventory_service.repository.InventoryRepository;
import com.primebasket.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;


    @Override
    public InventoryResponseDto addInventory(InventoryRequestDto requestDto) {

        if (inventoryRepository.existsByProductId(requestDto.getProductId())) {
            throw new InventoryAlreadyExistsException(
                    "Inventory already exists for product " + requestDto.getProductId());
        }

        Inventory inventory=InventoryMapper.dtoToEnt(requestDto);
        Inventory savedInventory=inventoryRepository.save(inventory);

        return InventoryMapper.entToDto(savedInventory);
    }
}
