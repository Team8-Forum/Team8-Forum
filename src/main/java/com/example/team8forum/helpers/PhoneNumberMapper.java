package com.example.team8forum.helpers;

import com.example.team8forum.models.PhoneNumber;
import com.example.team8forum.models.dtos.PhoneNumberDto;
import com.example.team8forum.services.contracts.PhoneNumberService;
import com.example.team8forum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PhoneNumberMapper {

    private final PhoneNumberService phoneNumberService;
    private final UserService userService;

    @Autowired
    public PhoneNumberMapper(PhoneNumberService phoneNumberService, UserService userService) {
        this.phoneNumberService = phoneNumberService;
        this.userService = userService;
    }

    public PhoneNumber dtoToObject(PhoneNumberDto phoneNumberDto) {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setPhoneNumber(phoneNumberDto.getPhoneNumber());
        return phoneNumber;
    }
}
