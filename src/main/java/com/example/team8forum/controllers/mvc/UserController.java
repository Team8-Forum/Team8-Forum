package com.example.team8forum.controllers.mvc;

import com.example.team8forum.exceptions.AuthorizationException;
import com.example.team8forum.exceptions.EntityDuplicateException;
import com.example.team8forum.exceptions.EntityNotFoundException;
import com.example.team8forum.helpers.AuthenticationHelper;
import com.example.team8forum.helpers.PhoneNumberMapper;
import com.example.team8forum.helpers.UserMapper;
import com.example.team8forum.models.*;
import com.example.team8forum.models.dtos.ChangePasswordDto;
import com.example.team8forum.models.dtos.FilterUserDto;
import com.example.team8forum.models.dtos.PhoneNumberDto;
import com.example.team8forum.models.dtos.UserDto;
import com.example.team8forum.services.contracts.PhoneNumberService;
import com.example.team8forum.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.InputMismatchException;
import java.util.List;

@RequestMapping("/users")
@Controller
public class UserController {
    private final UserService userService;
    private final UserMapper mapper;
    private final PhoneNumberMapper phoneNumberMapper;
    private final PhoneNumberService phoneNumberService;
    private final AuthenticationHelper authenticationHelper;

    public UserController(UserService userService, UserMapper mapper, PhoneNumberMapper phoneNumberMapper,
                          PhoneNumberService phoneNumberService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.mapper = mapper;
        this.phoneNumberMapper = phoneNumberMapper;
        this.phoneNumberService = phoneNumberService;
        this.authenticationHelper = authenticationHelper;
    }
    @GetMapping
    public String showAllUsers(Model model, HttpSession session, FilterUserDto filterUserDto) {

        User executingUser;
        try {
            executingUser = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        try {
            UserFilterOptions userFilterOptions = new UserFilterOptions(
                    filterUserDto.getFirstName(),
                    filterUserDto.getLastName(),
                    filterUserDto.getEmail(),
                    filterUserDto.getUsername(),
                    filterUserDto.getSortBy(),
                    filterUserDto.getSortOrder());
            User user = authenticationHelper.tryGetCurrentUser(session);
            List<User> users = userService.getAll(userFilterOptions, user);
            model.addAttribute("users", users);
            model.addAttribute("filterUserDto", new FilterUserDto());

            return "UsersView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}")
    public String showUserProfile(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            User existingUser = userService.getById(id);
            User loggedUser = authenticationHelper.tryGetCurrentUser(session);
            String phoneNumber;
            try {
                phoneNumber = phoneNumberService.getByUserId(id).getPhoneNumber();
            } catch (EntityNotFoundException e) {
                if(existingUser.isAdmin()) {
                    phoneNumber = "none";
                } else {
                    phoneNumber = "only-user";
                }
            }
            model.addAttribute("user", existingUser);
            model.addAttribute("userId", id);
            model.addAttribute("phoneNumber", phoneNumber);
            model.addAttribute("isLoggedUserProfileOwner", existingUser == loggedUser);
            model.addAttribute("isUserAdmin", existingUser.isAdmin());
            return "ProfileView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditUserPage(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            User existingUser = userService.getById(id);
            model.addAttribute("user", existingUser);
            model.addAttribute("userId", existingUser.getId());
            return "EditProfileView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/update-password")
    public String showUpdatePasswordPage(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            User existingUser = userService.getById(id);
            model.addAttribute("password", new ChangePasswordDto());
            model.addAttribute("user", existingUser);
            return "EditPasswordView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/update-password")
    public String changePassword (@Valid @ModelAttribute("password") ChangePasswordDto changePasswordDto, BindingResult errors,
                                  @PathVariable int id, Model model, HttpSession session ) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        user = userService.getById(id);
        model.addAttribute("user", user);

        if(errors.hasErrors()) {
            return "EditPasswordView";
        }

        String oldPassword = changePasswordDto.getOldPassword();
        String newPassword = changePasswordDto.getNewPassword();
        String confirmPassword = changePasswordDto.getConfirmPassword();

        try {
            userService.changePassword(user, oldPassword, newPassword, confirmPassword);
            return "redirect:/users/" + user.getId();
        } catch (AuthorizationException | InputMismatchException e) {
            errors.rejectValue("newPassword", "password_mismatch", e.getMessage());
            return "EditPasswordView";
        }

    }

    @GetMapping("{id}/phone-number")
    public String createPhoneNumber (@PathVariable int id, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            User userToBeUpdated = userService.getById(id);
            model.addAttribute("phoneNumber", new PhoneNumberDto());
            model.addAttribute("user",userToBeUpdated);
            return "PhoneNumberView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("{id}/phone-number")
    public String createPhoneNumber (@Valid@ModelAttribute ("phoneNumber") PhoneNumberDto phoneNumberDTO, BindingResult errors,
                                     @PathVariable int id, Model model, HttpSession session ) {
        User executingUser;
        User userToBeUpdated = userService.getById(id);
        try {
            executingUser = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        if(errors.hasErrors()) {
            model.addAttribute("user", userToBeUpdated);
            return "PhoneNumberView";
        }
        try {
            PhoneNumber phoneNumber = phoneNumberMapper.dtoToObject(phoneNumberDTO);
            userService.createPhoneNumber(executingUser,id,phoneNumber);
            model.addAttribute("phoneNumber", phoneNumberDTO);
            model.addAttribute("user",userToBeUpdated);
            return "redirect:/users/" + id;
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityDuplicateException e) {
            model.addAttribute("user", userToBeUpdated);
            errors.rejectValue("phoneNumber", "duplicate_phoneNumber", e.getMessage());
            return "PhoneNumberView";
        }
    }

    @GetMapping("{id}/phone-number/update")
    public String updatePhoneNumber (@PathVariable int id, Model model, HttpSession session) {
        try {
            User executingUser = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            User userToBeUpdated = userService.getById(id);
            PhoneNumberDto phoneNumberDTO = new PhoneNumberDto();
            PhoneNumber phoneNumber = phoneNumberService.getByUserId(id);
            phoneNumberDTO.setPhoneNumber(phoneNumber.getPhoneNumber());
            model.addAttribute("phoneNumber", phoneNumberDTO);
            model.addAttribute("user", userToBeUpdated);
            return "PhoneNumberUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("{id}/phone-number/update")
    public String updatePhoneNumber (@PathVariable int id, @Valid @ModelAttribute ("phoneNumber") PhoneNumberDto phoneNumberDTO, BindingResult errors,
                                     Model model, HttpSession session) {
        User executingUser;
        User userToBeUpdated = userService.getById(id);
        try {
            executingUser = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        if(errors.hasErrors()) {
            model.addAttribute("user", userToBeUpdated);
            return "PhoneNumberUpdateView";
        }
        try {
            PhoneNumber phoneNumber = phoneNumberMapper.dtoToObject(phoneNumberDTO);
            userService.updatePhoneNumber(executingUser, id, phoneNumber);
            model.addAttribute("user",userToBeUpdated);
            return "redirect:/users/" + id;
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityDuplicateException e) {
            model.addAttribute("user", userToBeUpdated);
            errors.rejectValue("phoneNumber", "duplicate_phoneNumber", e.getMessage());
            return "PhoneNumberUpdateView";
        }
    }

    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable int id, @Valid @ModelAttribute("updateUser") UserDto userDto, BindingResult errors,
                             Model model, HttpSession session) {
        User executingUser;
        try {
            executingUser = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        if(errors.hasErrors()) {
            User existingUser = userService.getById(id);
            model.addAttribute("user", existingUser);
            model.addAttribute("userId", existingUser.getId());
            return "EditProfileView";
        }

        try {
            User userToBeUpdated = mapper.dtoToObject(userDto, id);
            userService.update(executingUser, userToBeUpdated);
            return "redirect:/users/" + id;
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }


    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable int id, Model model, HttpSession session) {
        User executingUser;
        try {
            executingUser = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            User user = userService.getById(id);
            userService.delete(user,id);
            return "redirect:/auth/logout";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/admin")
    public String adminUser(@PathVariable int id, Model model, HttpSession session) {
        User executingUser;
        try {
            executingUser = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            userService.updateAdmin(executingUser,id);
            return "redirect:/users/" + id;
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/block")
    public String blockUser(@PathVariable int id, Model model, HttpSession session) {

        User executingUser;
        try {
            executingUser = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            userService.updateBlocked(executingUser,id);
            return "redirect:/users/" + id;
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("isLoggedUserAdmin")
    public boolean populateIsLoggedUserAdmin(HttpSession session) {
        boolean admin = false;
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            if(user.isAdmin()) {
                admin = true;
            }
        } catch (AuthorizationException e) {
            return false;
        }
        return admin;
    }

    @ModelAttribute("getUserId")
    public int populateGetUser(HttpSession session) {
        return authenticationHelper.tryGetUserId(session);
    }
}
