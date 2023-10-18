package com.example.team8forum.services.contracts;

import com.example.team8forum.models.PhoneNumber;

public interface PhoneNumberService {

    PhoneNumber getByPhoneNumber(String number);

    PhoneNumber getByUserId(int id);
}
