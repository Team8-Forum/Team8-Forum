package com.example.team8forum.repositories.contracts;

import com.example.team8forum.models.User;

import java.util.List;

public interface UserRepository {

    List<User> getAll();

    User getById(int id);

    User getByUsername(String username);

    void create(User user);

    void update(User user);
}
