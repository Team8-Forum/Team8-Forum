package com.example.team8forum.services.contracts;

import com.example.team8forum.models.PhoneNumber;
import com.example.team8forum.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAll();

    User getById(int id);

    User getByUsername(String username);

    void create(User user);

    void createPhoneNumber(User executingUser, int id, PhoneNumber phoneToCreate);

    void update(User executingUser, User userToUpdate);

    User updateAdmin(User executingUser, int id);

    User updateBlocked(User executingUser, int id);

    void updatePhoneNumber(User executingUser, int id, PhoneNumber newPhone);

    PhoneNumber deletePhoneNumber(User executingUser, int userToBeUpdatedId);

    void changePassword(User user, String oldPassword, String newPassword, String confirmedNewPassword);
}
