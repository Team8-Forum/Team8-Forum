package com.example.team8forum.services;

import com.example.team8forum.exceptions.EntityDuplicateException;
import com.example.team8forum.exceptions.EntityNotFoundException;
import com.example.team8forum.models.PhoneNumber;
import com.example.team8forum.models.User;
import com.example.team8forum.models.UserFilterOptions;
import com.example.team8forum.repositories.contracts.PhoneNumberRepository;
import com.example.team8forum.repositories.contracts.UserRepository;
import com.example.team8forum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.team8forum.helpers.ValidationHelpers.validateUserIsAdmin;
import static com.example.team8forum.helpers.ValidationHelpers.validateUserIsAdminOrAccountOwner;

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
    public List<User> getAll(UserFilterOptions filterOptions, User user) {
        validateUserIsAdmin(user);
        return userRepository.getAll(filterOptions);
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
    public void createPhoneNumber(User executeUser, int id, PhoneNumber phoneToCreate) {
        User userToGetPhone = getById(id);
        validateUserIsAdmin(executeUser);
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
    public void update(User executeUser, User updateUser) {
        validateUserIsAdminOrAccountOwner(executeUser, updateUser);
        boolean duplicateExists = true;
        try {
            User existingUser = userRepository.getByUsername(updateUser.getUsername());
            if (existingUser.getId() == updateUser.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new EntityDuplicateException("User", "email", updateUser.getEmail());
        }
        userRepository.update(updateUser);
    }

    @Override
    public User updateAdmin(User executeUser, int id) {
        User userToBeUpdated = getById(id);
        validateUserIsAdmin(executeUser);
        boolean newAdminStatus = !userToBeUpdated.isAdmin();
        userToBeUpdated.setAdmin(newAdminStatus);
        userRepository.update(userToBeUpdated);
        return userToBeUpdated;
    }

    @Override
    public User updateBlocked(User executeUser, int id) {
        User userToBeUpdated = getById(id);
        validateUserIsAdmin(executeUser);
        boolean newBlockedStatus = !userToBeUpdated.isBlocked();
        userToBeUpdated.setBlocked(newBlockedStatus);
        userRepository.update(userToBeUpdated);
        return userToBeUpdated;
    }

    @Override
    public void updatePhoneNumber(User executeUser, int id, PhoneNumber newPhone) {
        User userToGetPhone = getById(id);
        validateUserIsAdmin(executeUser);
        validateUserIsAdmin(userToGetPhone);
        PhoneNumber oldPhone = phoneNumberRepository.getByUserId(userToGetPhone.getId());
        checkIfPhoneExists(newPhone);
        oldPhone.setPhoneNumber(newPhone.getPhoneNumber());
        phoneNumberRepository.update(oldPhone);
    }

    @Override
    public PhoneNumber deletePhoneNumber(User executeUser, int id) {
        User userToGetPhoneDeleted = getById(id);
        validateUserIsAdminOrAccountOwner(executeUser, userToGetPhoneDeleted);
        validateUserIsAdmin(userToGetPhoneDeleted);
        phoneNumberRepository.delete(userToGetPhoneDeleted.getPhoneNumber());
        return userToGetPhoneDeleted.getPhoneNumber();
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
