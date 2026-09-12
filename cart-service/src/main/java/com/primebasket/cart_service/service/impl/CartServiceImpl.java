package com.primebasket.cart_service.service.impl;

import com.primebasket.cart_service.clients.ProductClient;
import com.primebasket.cart_service.clients.UserClient;
import com.primebasket.cart_service.dto.*;
import com.primebasket.cart_service.entity.Cart;
import com.primebasket.cart_service.entity.CartItem;
import com.primebasket.cart_service.exception.ResourceNotFoundException;
import com.primebasket.cart_service.exception.ResourceNullException;
import com.primebasket.cart_service.mapper.CartMapper;
import com.primebasket.cart_service.repository.CartRepository;
import com.primebasket.cart_service.service.CartService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserClient userClient;
    private final ProductClient productClient;
    private final CartMapper cartMapper;
    @Override
    public CartResponseDto addToCart(CartRequestDto requestDto) {

        //Validate Request
        if(requestDto==null || requestDto.getUserId()==null){

            throw new ResourceNullException("User Id must not be null");

        }
        if(requestDto.getItems()==null || requestDto.getItems().isEmpty()){
            throw new ResourceNullException("Cart items must not be null or empty");
        }

        //Validate Quantities and Product locally
        for(CartItemRequestDto itemRequestDto: requestDto.getItems()){
            if(itemRequestDto==null || itemRequestDto.getProductId()==null){
                throw new ResourceNullException("Cart item and Product Id must not be null");
            }

            if(itemRequestDto.getQuantity()==null || itemRequestDto.getQuantity()<=0){
                throw new ResourceNullException("Quantity must be greater than 0");
            }
        }

        //Validate User
        UserResponseDto user=userClient.fetchUserById(requestDto.getUserId());

        if(user==null || !Boolean.TRUE.equals(user.getActive())){
            throw new ResourceNotFoundException("User not found with Id: "+requestDto.getUserId());
        }

        //Collect distinct product Ids
        Set<Long>requestedProductIds=requestDto.getItems()
                .stream()
                .map(CartItemRequestDto::getProductId)
                .collect(Collectors.toSet());

        //Make one batch call
        //Product service should return only active products
        List<ProductResponseDto>activeProducts=productClient.validateProducts(requestedProductIds);

        Set<Long>activeProductIds=activeProducts.stream()
                .map(ProductResponseDto::getProductId)
                .collect(Collectors.toSet());

        //Find missing or inactive products
        Set<Long>invalidProductIds=new HashSet<>(requestedProductIds);

        invalidProductIds.removeAll(activeProductIds);

        if(!invalidProductIds.isEmpty()){
            throw new ResourceNotFoundException("Products not found or inactive"+invalidProductIds);
        }

        //Product Validation succeeded

        /*
            Find existing cart by userId
            1. If no cart exists create new one
            2. If cart exists reuse it
         */
        Cart cart= cartRepository.findByUserId(requestDto.getUserId())
                .orElseGet(()->{
                    Cart newCart= new Cart();

                    newCart.setUserId(requestDto.getUserId());
                    newCart.setItems(new ArrayList<>());
                    return newCart;
                });

        for(CartItemRequestDto itemDto: requestDto.getItems()){

            CartItem existingItem=cart.getItems().stream()
                    .filter(item->item.getProductId().equals(itemDto.getProductId()))
                    .findFirst()
                    .orElse(null);

            if(existingItem!=null){
                existingItem.setQuantity(
                        existingItem.getQuantity()+ itemDto.getQuantity()
                );
            }
            else{
                CartItem cartItem= new CartItem();

                cartItem.setProductId(itemDto.getProductId());
                cartItem.setQuantity(itemDto.getQuantity());
                cartItem.setCart(cart);

                cart.getItems().add(cartItem);

            }
        }

        Cart savedCart= cartRepository.save(cart);
        return CartMapper.entToDto(savedCart);

    }
}
