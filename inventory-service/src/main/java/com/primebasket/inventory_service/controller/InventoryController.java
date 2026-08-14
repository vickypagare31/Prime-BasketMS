package com.primebasket.inventory_service.controller;

import com.primebasket.inventory_service.dto.InventoryRequestDto;
import com.primebasket.inventory_service.dto.InventoryResponseDto;
import com.primebasket.inventory_service.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<InventoryResponseDto>addInventory(@Valid @RequestBody InventoryRequestDto requestDto){

        InventoryResponseDto responseDto=inventoryService.addInventory(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }
}
