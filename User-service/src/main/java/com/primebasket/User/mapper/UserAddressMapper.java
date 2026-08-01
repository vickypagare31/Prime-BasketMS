package com.primebasket.User.mapper;

import com.primebasket.User.dto.AddressRequestDto;
import com.primebasket.User.dto.UserAddressResponseDto;
import com.primebasket.User.entity.Address;
import com.primebasket.User.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserAddressMapper {

    public static UserAddressResponseDto entToDto(User user){
        UserAddressResponseDto requestDto=new UserAddressResponseDto();

        requestDto.setFirstName(user.getFirstName());
        requestDto.setLastName(user.getLastName());
        requestDto.setEmail(user.getEmail());
        requestDto.setMobileNumber(user.getMobileNumber());
        requestDto.setActive(user.getIsActive());
        requestDto.setRole(user.getRole());

        List<AddressRequestDto> addresses=new ArrayList<>();

        for(Address address : user.getAddresses()){
            AddressRequestDto addressRequestDto=new AddressRequestDto();

            addressRequestDto.setAddressLine(address.getAddressLine());
            addressRequestDto.setCity(address.getCity());
            addressRequestDto.setAddressType(address.getAddressType());
            addressRequestDto.setStreet(address.getStreet());
            addressRequestDto.setCountry(address.getCountry());
            addressRequestDto.setLandmark(address.getLandmark());
            addressRequestDto.setPostalCode(address.getPostalCode());

            addresses.add(addressRequestDto);
        }

        requestDto.setAddressList(addresses);
        return  requestDto;
    }

}
