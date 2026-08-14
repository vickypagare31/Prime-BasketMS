package com.primebasket.inventory_service.service.impl;

import com.primebasket.inventory_service.dto.InventoryRequestDto;
import com.primebasket.inventory_service.entity.Inventory;
import com.primebasket.inventory_service.exception.InventoryAlreadyExistsException;
import com.primebasket.inventory_service.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @Test
    void addInventory_whenProductAlreadyHasInventory_throwsExceptionAndDoesNotSave() {
        InventoryRequestDto request = new InventoryRequestDto(101L, 20);
        when(inventoryRepository.existsByProductId(101L)).thenReturn(true);

        assertThrows(InventoryAlreadyExistsException.class, () -> inventoryService.addInventory(request));

        verify(inventoryRepository).existsByProductId(101L);
        verify(inventoryRepository, never()).save(org.mockito.ArgumentMatchers.any(Inventory.class));
    }
}
