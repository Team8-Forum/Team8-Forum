package com.example.team8forum.controllers.rest;

import com.example.team8forum.exceptions.AuthorizationException;
import com.example.team8forum.exceptions.EntityDuplicateException;
import com.example.team8forum.exceptions.EntityNotFoundException;
import com.example.team8forum.helpers.AuthenticationHelper;
import com.example.team8forum.helpers.PhoneNumberMapper;
import com.example.team8forum.helpers.UserMapper;
import com.example.team8forum.models.PhoneNumber;
import com.example.team8forum.models.User;
import com.example.team8forum.models.UserFilterOptions;
import com.example.team8forum.models.dtos.PhoneNumberDto;
import com.example.team8forum.models.dtos.RegisterUserDto;
import com.example.team8forum.models.dtos.UserDto;
import com.example.team8forum.services.contracts.PhoneNumberService;
import com.example.team8forum.services.contracts.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final PhoneNumberService phoneNumberService;
    private final PhoneNumberMapper phoneNumberMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserRestController(UserService userService, UserMapper userMapper,
                              PhoneNumberService phoneNumberService, PhoneNumberMapper phoneNumberMapper,
                              AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.phoneNumberService = phoneNumberService;
        this.phoneNumberMapper = phoneNumberMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<User> getAll(
            @RequestHeader HttpHeaders headers,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder){
        try{
            User user = authenticationHelper.tryGetUser(headers);
            authenticationHelper.verifyAuthentication(user.getUsername(),user.getPassword());
            UserFilterOptions userFilterOptions =
                    new UserFilterOptions(firstName, lastName, email, username, sortBy, sortOrder);
            return userService.getAll(userFilterOptions, user);
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable int id, @RequestHeader HttpHeaders headers){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            authenticationHelper.verifyAuthentication(user.getUsername(),user.getPassword());
            return userService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public User create(@Valid @RequestBody RegisterUserDto registerUserDto){
        try{
            User user = userMapper.registerDTOToObject(registerUserDto);
            userService.create(user);
            return user;
        } catch (EntityDuplicateException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("/{id}/phone-number")
    public PhoneNumber createPhoneNumber(@RequestHeader HttpHeaders headers, @PathVariable int id,
                                  @Valid @RequestBody PhoneNumberDto phoneNumberDto){
        try{
            User executeUser = authenticationHelper.tryGetUser(headers);
            authenticationHelper.verifyAuthentication(executeUser.getUsername(),executeUser.getPassword());
            PhoneNumber phoneNumber = phoneNumberMapper.dtoToObject(phoneNumberDto);
            userService.createPhoneNumber(executeUser,id,phoneNumber);
            return phoneNumber;
        } catch (EntityDuplicateException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public User update(@RequestHeader HttpHeaders headers, @PathVariable int id,
                       @Valid @RequestBody UserDto userDto){
        try {
            User executeUser = authenticationHelper.tryGetUser(headers);
            authenticationHelper.verifyAuthentication(executeUser.getUsername(), executeUser.getPassword());
            User updateUser = userMapper.dtoToObject(userDto,id);
            userService.update(executeUser,updateUser);
            return updateUser;
        } catch (EntityDuplicateException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}/phone-number")
    public PhoneNumber updatePhoneNumber(@RequestHeader HttpHeaders headers, @PathVariable int id,
                                         @Valid @RequestBody PhoneNumberDto phoneNumberDto){
        try {
            User executeUser = authenticationHelper.tryGetUser(headers);
            PhoneNumber phoneNumber = phoneNumberMapper.dtoToObject(phoneNumberDto);
            userService.updatePhoneNumber(executeUser,id,phoneNumber);
            return phoneNumberService.getByPhoneNumber(phoneNumber.getPhoneNumber());
        } catch (EntityDuplicateException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}/admin")
    public User updateAdmin(@RequestHeader HttpHeaders headers,@PathVariable int id){
        try {
            User executeUser = authenticationHelper.tryGetUser(headers);
            User updateUser = userService.updateAdmin(executeUser,id);
            return updateUser;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}/blocked")
    public User updateBlocked(@RequestHeader HttpHeaders headers,@PathVariable int id){
        try {
            User executeUser = authenticationHelper.tryGetUser(headers);
            User updateUser = userService.updateBlocked(executeUser,id);
            return updateUser;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deletePhoneNumber(@RequestHeader HttpHeaders headers, @PathVariable int id){
        try {
            User executeUser = authenticationHelper.tryGetUser(headers);
            userService.deletePhoneNumber(executeUser,id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
