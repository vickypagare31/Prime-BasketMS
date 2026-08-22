package com.primebasket.inventory_service.mapper;

import com.primebasket.inventory_service.dto.InventoryRequestDto;
import com.primebasket.inventory_service.dto.InventoryResponseDto;
import com.primebasket.inventory_service.dto.InventoryUpdateResponseDto;
import com.primebasket.inventory_service.entity.Inventory;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    public static InventoryResponseDto entToDto(Inventory inventory){
        InventoryResponseDto responseDto=new InventoryResponseDto();

        responseDto.setInventoryId(inventory.getInventoryId());
        responseDto.setProductId(inventory.getProductId());
        responseDto.setQuantity(inventory.getQuantity());
        responseDto.setReservedQuantity(inventory.getReservedQuantity());
        responseDto.setCreatedAt(inventory.getCreatedAt());
        return responseDto;
    }

    public static Inventory dtoToEnt(InventoryRequestDto requestDto){
        Inventory inventory=new Inventory();

        inventory.setProductId(requestDto.getProductId());
        inventory.setQuantity(requestDto.getQuantity());
        return  inventory;
    }

    public static InventoryUpdateResponseDto entToUpdateDto(Inventory inventory){
        InventoryUpdateResponseDto responseDto=new InventoryUpdateResponseDto();

        responseDto.setInventoryId(inventory.getInventoryId());
        responseDto.setProductId(inventory.getProductId());
        responseDto.setQuantity(inventory.getQuantity());
        responseDto.setReservedQuantity(inventory.getReservedQuantity());
        responseDto.setUpdatedAt(inventory.getUpdatedAt());
        return responseDto;
    }
}
