package com.primebasket.User.service;

import com.primebasket.User.dto.AddressRequestDto;
import com.primebasket.User.dto.AddressResponseDto;
import com.primebasket.User.dto.UserResponseDto;

public interface AddressService {

    AddressResponseDto addAddress(Long userId, AddressRequestDto addressRequestDto);
}
