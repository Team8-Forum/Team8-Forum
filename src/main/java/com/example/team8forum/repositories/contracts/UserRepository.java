package com.example.team8forum.repositories.contracts;

import com.example.team8forum.models.User;
import com.example.team8forum.models.UserFilterOptions;

import java.util.List;

public interface UserRepository {

    List<User> getAll(UserFilterOptions filterOptions);

    User getById(int id);

    User getByUsername(String username);

    void create(User user);

    void update(User user);
}
