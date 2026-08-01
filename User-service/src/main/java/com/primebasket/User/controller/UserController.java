package com.primebasket.User.controller;

import com.primebasket.User.dto.*;
import com.primebasket.User.entity.User;
import com.primebasket.User.service.AddressService;
import com.primebasket.User.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final AddressService addressService;


    @PostMapping("/register")
    public ResponseEntity<UserResponseDto>registerUser(@Valid @RequestBody UserRequestDto userRequestDto){
    UserResponseDto response=userService.registerUser(userRequestDto);
    return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{userId}/addresses")
    public ResponseEntity<AddressResponseDto>addAddress(@PathVariable Long userId, @RequestBody AddressRequestDto addressRequestDto){
        AddressResponseDto response=addressService.addAddress(userId, addressRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{userId}/addresses")
    public ResponseEntity<UserAddressResponseDto>getUserById(@PathVariable Long userId){
        UserAddressResponseDto userAddressResponseDto=userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userAddressResponseDto);

    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserUpdateDto>updateUserById(@PathVariable Long userId, @Valid @RequestBody UserUpdateDto userUpdateDto){
        UserUpdateDto updatedUser=userService.updateUserById(userId,userUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String>deleteUserById(@PathVariable Long userId){
        userService.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<Page<UserResponseDto>>getAllUsers(@RequestParam(defaultValue = "0")int page,
                                            @RequestParam(defaultValue = "5")int size){
        Page<UserResponseDto>userList=userService.getAllUsers(page,size);
        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }



}
