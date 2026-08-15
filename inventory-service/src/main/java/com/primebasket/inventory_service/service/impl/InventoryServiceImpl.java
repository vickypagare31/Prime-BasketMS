package com.primebasket.inventory_service.service.impl;

import com.primebasket.inventory_service.dto.InventoryRequestDto;
import com.primebasket.inventory_service.dto.InventoryResponseDto;
import com.primebasket.inventory_service.entity.Inventory;
import com.primebasket.inventory_service.exception.InvalidQuantityException;
import com.primebasket.inventory_service.exception.InventoryAlreadyExistsException;
import com.primebasket.inventory_service.exception.ResourceNotFoundException;
import com.primebasket.inventory_service.exception.ResourceNullException;
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

    @Override
    public InventoryResponseDto getInventory(Long productId) {

        Inventory inventory=inventoryRepository.findByProductId(productId)
                .orElseThrow(()->new ResourceNotFoundException("Inventory not found for this productId: "+productId));

        return InventoryMapper.entToDto(inventory);
    }

    @Override
    public InventoryResponseDto updateStock(Long productId, InventoryRequestDto requestDto) {

        Inventory inventory=inventoryRepository.findByProductId(productId)
                .orElseThrow(()->new ResourceNotFoundException(
                        "Inventory not found for this productId: "+productId));
        if(requestDto.getQuantity()==null){
            throw new ResourceNullException("Quantity should not be null");
        }

        if(requestDto.getQuantity()<=0){
            throw new InvalidQuantityException("Quantity must be greater than 0");
        }

        inventory.setQuantity(inventory.getQuantity() + requestDto.getQuantity());
        inventoryRepository.save(inventory);
        return InventoryMapper.entToDto(inventory);
    }

    @Override
    public InventoryResponseDto deductStock(Long productId, InventoryRequestDto requestDto) {

        Inventory inventory=inventoryRepository.findByProductId(productId)
                .orElseThrow(()->new ResourceNotFoundException("Inventory not found for this productId: "+productId));

        if(requestDto.getQuantity()==null){
            throw new ResourceNullException("Quantity should not be null");
        }
        if(requestDto.getQuantity()<=0){
            throw new InvalidQuantityException("Quantity must be greater than 0");
        }
        if(requestDto.getQuantity() > inventory.getQuantity()){
            throw new InvalidQuantityException("Insufficient stock, Available Quantity: "+inventory.getQuantity());
        }
        inventory.setQuantity(inventory.getQuantity()-requestDto.getQuantity());
        inventoryRepository.save(inventory);
        return InventoryMapper.entToDto(inventory);
    }

    @Override
    public InventoryResponseDto reserveStock(Long productId, InventoryRequestDto requestDto) {

        Inventory inventory=inventoryRepository.findByProductId(productId)
                .orElseThrow(()->new ResourceNotFoundException("Inventory not found for this productId: "+productId));

        if(requestDto.getQuantity()==null){
            throw new ResourceNullException("Quantity should not be null");
        }

        if(requestDto.getQuantity()<=0){
            throw new InvalidQuantityException("Quantity must be greater than 0");
        }

        int availableStock=inventory.getQuantity()- inventory.getReservedQuantity();
        /*
            inventory=17
            reservedQuantity/requested quantity = 3
            available = 14
         */

        if(requestDto.getQuantity()>availableStock){
            throw new InvalidQuantityException("Insufficient available stock. Available Stock: "+availableStock);
        }

        inventory.setReservedQuantity(inventory.getReservedQuantity()+requestDto.getQuantity());
        inventoryRepository.save(inventory);
        return InventoryMapper.entToDto(inventory);
    }

    @Override
    public InventoryResponseDto releaseReservedStock(Long productId, InventoryRequestDto requestDto) {

        Inventory inventory=inventoryRepository.findByProductId(productId)
                .orElseThrow(()->new ResourceNotFoundException("Inventory not found for this productId: "+productId));

        if(requestDto.getQuantity()==null){
            throw new ResourceNullException("Quantity should not be null");
        }

        if(requestDto.getQuantity()<=0){
            throw new InvalidQuantityException("Quantity must be greater than 0");
        }

        if(requestDto.getQuantity()>inventory.getReservedQuantity()){
            throw new InvalidQuantityException("Cannot release more than reserved quantity. "+"Reserved Quantity: "+inventory.getReservedQuantity());
        }
        inventory.setReservedQuantity(inventory.getReservedQuantity() - requestDto.getQuantity());
        inventoryRepository.save(inventory);
        return InventoryMapper.entToDto(inventory);
    }
}
