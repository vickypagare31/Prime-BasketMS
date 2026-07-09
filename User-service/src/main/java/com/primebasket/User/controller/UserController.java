package com.primebasket.User.controller;

import com.primebasket.User.dto.UserRequestDto;
import com.primebasket.User.dto.UserResponseDto;
import com.primebasket.User.entity.User;
import com.primebasket.User.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prime-basket/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<String>createNewUser(@RequestBody UserRequestDto userRequestDto){
    userService.createUser(userRequestDto);
    return  new ResponseEntity<>("User Created Successfully",HttpStatus.CREATED);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<Page<UserResponseDto>>getAllUsers(@RequestParam(defaultValue = "0")int page,
                                            @RequestParam(defaultValue = "5")int size){
        Page<UserResponseDto>userList=userService.getAllUsers(page,size);
        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto>getUserById(@PathVariable Long userId){
        UserResponseDto userResponseDto=userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);

    }
}
