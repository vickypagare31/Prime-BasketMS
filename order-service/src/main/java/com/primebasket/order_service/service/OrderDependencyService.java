package com.primebasket.order_service.service;

import com.primebasket.order_service.dto.*;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrderDependencyService {

    CartResponseDto getCartByUserId(@PathVariable Long userId);

    ProductResponseDto getProductById(@PathVariable Long productId);

    InventoryUpdateResponseDto reserveStock(@PathVariable Long productId, @RequestBody StockQuantityRequestDto requestDto);

    InventoryUpdateResponseDto releaseReservedStock(@PathVariable Long productId, @RequestBody StockQuantityRequestDto requestDto);
}
