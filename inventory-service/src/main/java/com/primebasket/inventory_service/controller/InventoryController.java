package com.primebasket.inventory_service.controller;

import com.primebasket.inventory_service.dto.InventoryRequestDto;
import com.primebasket.inventory_service.dto.InventoryResponseDto;
import com.primebasket.inventory_service.dto.InventoryUpdateResponseDto;
import com.primebasket.inventory_service.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponseDto>getInventory(@PathVariable Long productId){
        InventoryResponseDto responseDto=inventoryService.getInventory(productId);

        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{productId}/stock")
    public ResponseEntity<InventoryUpdateResponseDto>updateStock(@PathVariable Long productId, @RequestBody InventoryRequestDto requestDto){
        InventoryUpdateResponseDto responseDto=inventoryService.updateStock(productId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{productId}/stock/reduce")
    public ResponseEntity<InventoryUpdateResponseDto>deductStock(@PathVariable Long productId, @RequestBody InventoryRequestDto requestDto){
        InventoryUpdateResponseDto responseDto=inventoryService.deductStock(productId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{productId}/stock/reserve")
    public ResponseEntity<InventoryUpdateResponseDto>reserveStock(@PathVariable Long productId, @RequestBody InventoryRequestDto requestDto){
        InventoryUpdateResponseDto responseDto=inventoryService.reserveStock(productId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{productId}/stock/release")
    public ResponseEntity<InventoryUpdateResponseDto>releaseReservedStock(@PathVariable Long productId, @RequestBody InventoryRequestDto requestDto){
        InventoryUpdateResponseDto responseDto=inventoryService.releaseReservedStock(productId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{productId}/stock/confirm")
    public ResponseEntity<InventoryUpdateResponseDto>confirmReservedStock(@PathVariable Long productId, @RequestBody InventoryRequestDto requestDto){
        InventoryUpdateResponseDto responseDto=inventoryService.confirmReservedStock(productId, requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
