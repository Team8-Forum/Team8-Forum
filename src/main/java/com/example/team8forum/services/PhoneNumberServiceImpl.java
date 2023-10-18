package com.example.team8forum.services;


import com.example.team8forum.models.PhoneNumber;
import com.example.team8forum.repositories.contracts.PhoneNumberRepository;
import com.example.team8forum.services.contracts.PhoneNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhoneNumberServiceImpl implements PhoneNumberService {
    private final PhoneNumberRepository repository;

    @Autowired
    public PhoneNumberServiceImpl(PhoneNumberRepository repository) {
        this.repository = repository;
    }

    @Override
    public PhoneNumber getByPhoneNumber(String number) {
        return repository.getByPhoneNumber(number);
    }

    @Override
    public PhoneNumber getByUserId(int id) {
        return repository.getByUserId(id);
    }
}
