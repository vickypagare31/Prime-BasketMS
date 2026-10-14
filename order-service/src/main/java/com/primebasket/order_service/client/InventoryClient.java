package com.primebasket.order_service.client;

import com.primebasket.order_service.dto.InventoryUpdateResponseDto;
import com.primebasket.order_service.dto.StockQuantityRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @PatchMapping("/api/v1/inventory/{productId}/stock/reserve")
    InventoryUpdateResponseDto reserveStock(@PathVariable Long productId, @RequestBody StockQuantityRequestDto requestDto);

    @PatchMapping("/api/v1/inventory/{productId}/stock/release")
    InventoryUpdateResponseDto releaseReservedStock(@PathVariable Long productId, @RequestBody StockQuantityRequestDto requestDto);
}
