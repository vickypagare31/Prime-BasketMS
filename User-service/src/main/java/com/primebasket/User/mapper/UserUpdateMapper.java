package com.primebasket.User.mapper;

import com.primebasket.User.dto.UserUpdateDto;
import com.primebasket.User.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserUpdateMapper {

    public static User dtoToEnt(UserUpdateDto requestDto){

        User user=new User();

        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setMobileNumber(requestDto.getMobileNumber());

        return user;
    }

    public static UserUpdateDto entToDto(User user) {

        UserUpdateDto updateDto=new UserUpdateDto();

        updateDto.setFirstName(user.getFirstName());
        updateDto.setLastName(user.getLastName());
        updateDto.setMobileNumber(user.getMobileNumber());

        return updateDto;
    }
}
