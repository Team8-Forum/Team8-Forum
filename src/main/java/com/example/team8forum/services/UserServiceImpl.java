package com.example.team8forum.services;

import com.example.team8forum.exceptions.AuthorizationException;
import com.example.team8forum.exceptions.EntityDuplicateException;
import com.example.team8forum.exceptions.EntityNotFoundException;
import com.example.team8forum.models.PhoneNumber;
import com.example.team8forum.models.User;
import com.example.team8forum.repositories.contracts.PhoneNumberRepository;
import com.example.team8forum.repositories.contracts.UserRepository;
import com.example.team8forum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;
import java.util.List;

import static com.example.team8forum.helpers.ValidationHelpers.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PhoneNumberRepository phoneNumberRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PhoneNumberRepository phoneNumberRepository) {
        this.userRepository = userRepository;
        this.phoneNumberRepository = phoneNumberRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public void create(User user) {
        checkIfUserIsUnique(user);
        userRepository.create(user);
    }

    @Override
    public void createPhoneNumber(User executingUser, int id, PhoneNumber phoneToCreate) {
        User userToGetPhone = getById(id);
        validateUserIsAdmin(executingUser);
        validateUserIsAdmin(userToGetPhone);
        boolean duplicateExists = true;
        try {
            phoneNumberRepository.getByUserId(userToGetPhone.getId());
        } catch (EntityNotFoundException e) {
            duplicateExists=false;
        }
        if (duplicateExists) {
            throw new EntityDuplicateException("This user is already", "phone number", userToGetPhone.getPhoneNumber().getPhoneNumber());
        }
        checkIfPhoneExists(phoneToCreate);
        phoneToCreate.setUserId(userToGetPhone.getId());
        phoneNumberRepository.create(phoneToCreate);
    }

    @Override
    public void update(User executingUser, User userToUpdate) {
        validateUserIsAdminOrAccountOwner(executingUser, userToUpdate);
        boolean duplicateExists = true;
        try {
            User existingUser = userRepository.getByUsername(userToUpdate.getUsername());
            if (existingUser.getId() == userToUpdate.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new EntityDuplicateException("User", "email", userToUpdate.getEmail());
        }
        userRepository.update(userToUpdate);
    }

    @Override
    public User updateAdmin(User executingUser, int id) {
        User userToBeUpdated = getById(id);
        validateUserIsAdmin(executingUser);
        boolean newAdminStatus = !userToBeUpdated.isAdmin();
        userToBeUpdated.setAdmin(newAdminStatus);
        userRepository.update(userToBeUpdated);
        return userToBeUpdated;
    }

    @Override
    public User updateBlocked(User executingUser, int id) {
        User userToBeUpdated = getById(id);
        validateUserIsAdmin(executingUser);
        boolean newBlockedStatus = !userToBeUpdated.isBlocked();
        userToBeUpdated.setBlocked(newBlockedStatus);
        userRepository.update(userToBeUpdated);
        return userToBeUpdated;
    }

    @Override
    public void updatePhoneNumber(User executingUser, int id, PhoneNumber newPhone) {
        User userToGetPhone = getById(id);
        validateUserIsAdmin(executingUser);
        validateUserIsAdmin(userToGetPhone);
        PhoneNumber oldPhone = phoneNumberRepository.getByUserId(userToGetPhone.getId());
        checkIfPhoneExists(newPhone);
        oldPhone.setPhoneNumber(newPhone.getPhoneNumber());
        phoneNumberRepository.update(oldPhone);
    }

    @Override
    public PhoneNumber deletePhoneNumber(User executingUser, int userToBeUpdatedId) {
        User userToGetPhoneDeleted = getById(userToBeUpdatedId);
        validateUserIsAdminOrAccountOwner(executingUser, userToGetPhoneDeleted);
        validateUserIsAdmin(userToGetPhoneDeleted);
        phoneNumberRepository.delete(userToGetPhoneDeleted.getPhoneNumber());
        return userToGetPhoneDeleted.getPhoneNumber();
    }

    @Override
    public void changePassword(User user, String oldPassword, String newPassword, String confirmedNewPassword) {
        if(!user.getPassword().equals(oldPassword)) {
            throw new AuthorizationException("Your old password is incorrect");
        }

        if(!newPassword.equals(confirmedNewPassword)) {
            throw new InputMismatchException("New password do not match with confirmed one");
        }
    }

    private void checkIfUserIsUnique(User user) {
        boolean duplicateExists = true;
        try {
            userRepository.getByUsername(user.getUsername());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("User", "email", user.getEmail());
        }

        duplicateExists = true;

        try {
            userRepository.getByUsername(user.getUsername());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }
    }

    private void checkIfPhoneExists(PhoneNumber phoneToCreate) {
        boolean duplicateExists = true;
        try {
            phoneNumberRepository.getByPhoneNumber(phoneToCreate.getPhoneNumber());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new EntityDuplicateException("User", "phone-number", phoneToCreate.getPhoneNumber());
        }
    }
}
