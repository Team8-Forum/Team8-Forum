package com.example.team8forum.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PhoneNumberDto {

    @NotNull(message = "Phone number should not be null")
    @Size(min = 8, max = 10, message = "Phone number should be between 8 and 10 symbols")
    private String phoneNumber;

    public PhoneNumberDto(){
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
