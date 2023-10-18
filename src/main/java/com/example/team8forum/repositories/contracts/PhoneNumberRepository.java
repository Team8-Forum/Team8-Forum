package com.example.team8forum.repositories.contracts;

import com.example.team8forum.models.PhoneNumber;

public interface PhoneNumberRepository {

    PhoneNumber create(PhoneNumber phoneNumber);

    void update(PhoneNumber phoneNumber);

    void delete(PhoneNumber phoneNumber);

    PhoneNumber getByPhoneNumber(String number);

    PhoneNumber getByUserId (int id);
}
