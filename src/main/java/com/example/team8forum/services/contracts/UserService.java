package com.example.team8forum.services.contracts;

import com.example.team8forum.models.PhoneNumber;
import com.example.team8forum.models.User;
import com.example.team8forum.models.UserFilterOptions;

import java.util.List;

public interface UserService {

    List<User> getAll();
    List<User> getAll(UserFilterOptions filterOptions,User user);

    User getById(int id);

    User getByUsername(String username);

    void create(User user);

    void createPhoneNumber(User executingUser, int id, PhoneNumber phoneToCreate);

    void update(User executingUser, User userToUpdate);

    User updateAdmin(User executingUser, int id);

    User updateBlocked(User executingUser, int id);

    void updatePhoneNumber(User executingUser, int id, PhoneNumber newPhone);

    void changePassword(User user, String oldPassword, String newPassword, String confirmedNewPassword);

    PhoneNumber deletePhoneNumber(User executingUser, int userToBeUpdatedId);

    void delete(User executingUser, int id);
}
