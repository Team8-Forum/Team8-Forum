package com.example.team8forum;

import com.example.team8forum.exceptions.AuthorizationException;
import com.example.team8forum.exceptions.EntityDuplicateException;
import com.example.team8forum.exceptions.EntityNotFoundException;
import com.example.team8forum.models.PhoneNumber;
import com.example.team8forum.models.User;
import com.example.team8forum.repositories.contracts.PhoneNumberRepository;
import com.example.team8forum.repositories.contracts.UserRepository;
import com.example.team8forum.services.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static com.example.team8forum.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    UserRepository mockRepository;

    @Mock
    PhoneNumberRepository phoneNumberRepository;

    @InjectMocks
    UserServiceImpl service;

    @Test
    public void getById_should_return_User_when_userExists() {
        User mockUser = createMockUser();
        Mockito.when(mockRepository.getById(mockUser.getId()))
                .thenReturn(mockUser);

        User result = service.getById(mockUser.getId());

        Assertions.assertAll(
                () -> Assertions.assertEquals(mockUser.getId(), result.getId()),
                () -> Assertions.assertEquals(mockUser.getEmail(), result.getEmail()),
                () -> Assertions.assertEquals(mockUser.getUsername(), result.getUsername()),
                () -> Assertions.assertEquals(mockUser.isAdmin(), result.isAdmin()),
                () -> Assertions.assertEquals(mockUser.getPassword(), result.getPassword())
        );
    }

    @Test
    public void getByUsername_should_return_User_when_userExists() {
        User mockUser = createMockUser();
        Mockito.when(mockRepository.getByUsername(mockUser.getUsername()))
                .thenReturn(mockUser);

        User result = service.getByUsername(mockUser.getUsername());


        Assertions.assertAll(
                () -> Assertions.assertEquals(mockUser.getId(), result.getId()),
                () -> Assertions.assertEquals(mockUser.getEmail(), result.getEmail()),
                () -> Assertions.assertEquals(mockUser.getUsername(), result.getUsername()),
                () -> Assertions.assertEquals(mockUser.isAdmin(), result.isAdmin()),
                () -> Assertions.assertEquals(mockUser.getPassword(), result.getPassword())
        );
    }

    @Test
    public void create_should_throw_when_userWithSameEmailExists() {
        User mockUser = createMockUser();

        Mockito.when(mockRepository.getByUsername(mockUser.getUsername()))
                .thenReturn(mockUser);

        Assertions.assertThrows(EntityDuplicateException.class, () -> service.create(mockUser));
    }

    @Test
    public void createPhoneNumber_should_throw_when_phoneNumberAlreadyExists() {
        User mockAdminToExecute = createMockAdmin();
        User mockAdminToReceivePhone = createMockAdmin();
        PhoneNumber mockPhoneNumber = createMockPhoneNumber();

        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockAdminToReceivePhone);

        Mockito.when(phoneNumberRepository.getByUserId(Mockito.anyInt()))
                .thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityDuplicateException.class,
                () -> service.createPhoneNumber(mockAdminToExecute, mockAdminToReceivePhone.getId(), mockPhoneNumber));
    }

    @Test
    public void createPhoneNumber_should_callRepository_when_theSamePhoneNumberDoesNotExist() {
        User mockAdminToExecute = createMockAdmin();
        User mockAdminToGetPhone = createMockAdmin();
        PhoneNumber mockPhoneNumber = createMockPhoneNumber();

        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockAdminToGetPhone);
        Mockito.when(phoneNumberRepository.getByUserId(Mockito.anyInt()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(phoneNumberRepository.getByPhoneNumber(mockPhoneNumber.getPhoneNumber()))
                .thenThrow(EntityNotFoundException.class);

        service.createPhoneNumber(mockAdminToExecute, mockAdminToGetPhone.getId(), mockPhoneNumber);


        Mockito.verify(phoneNumberRepository, Mockito.times(1))
                .create(mockPhoneNumber);
    }

    @Test
    public void update_should_throwException_when_userEmailAlreadyExists() {
        User mockUserObject = createMockUserWithId2();
        User mockInitiator = createMockAdmin();
        mockUserObject.setEmail("mock@user.com");

        Mockito.when(mockRepository.getByUsername(Mockito.anyString()))
                .thenReturn(mockInitiator);

        Assertions.assertThrows(EntityDuplicateException.class,
                () -> service.update(mockInitiator, mockUserObject));
    }

    @Test
    public void update_should_return_updateUser_when_userIsAdmin() {
        User mockInitiator = createMockAdmin();
        User mockUserObject = createMockUserWithId2();

        Mockito.when(mockRepository.getByUsername(mockUserObject.getUsername()))
                .thenThrow(EntityNotFoundException.class);

        service.update(mockInitiator, mockUserObject);

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockUserObject);

    }

    @Test
    public void updateAdmin_should_throw_when_executingUserIsNotAdmin() {
        User executingUser = createMockUserWithId2();
        User mockUserToBeUpdated = createMockUser();

        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockUserToBeUpdated);

        Assertions.assertThrows(AuthorizationException.class,
                () -> service.updateAdmin(executingUser, mockUserToBeUpdated.getId()));
    }

    @Test
    public void updateBlocked_should_throw_when_executingUserIsNotAdmin() {
        // Arrange
        User executingUser = createMockUserWithId2();
        User mockUserToBeUpdated = createMockUser();

        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockUserToBeUpdated);

        Assertions.assertThrows(AuthorizationException.class,
                () -> service.updateBlocked(executingUser, mockUserToBeUpdated.getId()));
    }

    @Test
    public void updateBlocked_should_return_block_when_executingUserIsAdmin() {
        // Arrange
        User executingUser = createMockAdmin();
        User mockUserToBeUpdated = createMockUser();

        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockUserToBeUpdated);

        service.updateBlocked(executingUser, mockUserToBeUpdated.getId());

        Assertions.assertTrue(mockUserToBeUpdated.isBlocked());
    }
    @Test
    public void updatePhoneNumber_should_return_throw_when_executingUser_isNotAdmin() {
        User executingUser = createMockUserWithId2();
        User mockUserToBeUpdated = createMockAdmin();
        PhoneNumber mockPhone = createMockPhoneNumber();

        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockUserToBeUpdated);

        Assertions.assertThrows(AuthorizationException.class,
                () -> service.updatePhoneNumber(executingUser, mockUserToBeUpdated.getId(), mockPhone));
    }

    @Test
    public void updatePhoneNumber_should_return_throw_when_PhoneAlreadyExists() {
        User executingUser = createMockAdmin();
        User mockUserToBeUpdated = createMockAdmin();
        PhoneNumber mockPhone = createMockPhoneNumber();

        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockUserToBeUpdated);

        Assertions.assertThrows(EntityDuplicateException.class,
                () -> service.updatePhoneNumber(executingUser, mockUserToBeUpdated.getId(), mockPhone));
    }
}
