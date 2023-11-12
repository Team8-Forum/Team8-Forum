package com.example.team8forum.controllers.mvc;

import com.example.team8forum.exceptions.AuthorizationException;
import com.example.team8forum.exceptions.EntityDuplicateException;
import com.example.team8forum.helpers.AuthenticationHelper;
import com.example.team8forum.helpers.UserMapper;
import com.example.team8forum.models.User;
import com.example.team8forum.models.dtos.LoginDto;
import com.example.team8forum.models.dtos.RegisterUserDto;
import com.example.team8forum.services.contracts.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserMapper userMapper;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public AuthenticationController(UserMapper userMapper, UserService userService, AuthenticationHelper authenticationHelper) {
        this.userMapper = userMapper;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
            return "redirect:/";
        } catch (AuthorizationException e) {
            //return "redirect:/auth/login";
        }
        model.addAttribute("login", new LoginDto());
        return "LoginView";
    }

    @PostMapping("/login")
    public String handleLoginPage(@ModelAttribute("login") LoginDto loginUserDto, BindingResult bindingResult,
                                  HttpSession session, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return "LoginView";
        }

        try {
            authenticationHelper.verifyAuthentication(loginUserDto.getUsername(), loginUserDto.getPassword());
            session.setAttribute("currentUser", loginUserDto.getUsername());
            Cookie cookie = new Cookie("user", loginUserDto.getUsername());
            cookie.setSecure(false);
            response.addCookie(cookie);
            return "redirect:/";
        } catch (AuthorizationException e) {
            bindingResult.rejectValue("username", "auth_error", e.getMessage());
            return "LoginView";
        }
    }
    @GetMapping("/logout")
    public String handleLogout(HttpSession session, HttpServletResponse response) {
        session.removeAttribute("currentUser");
        response.reset();
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new RegisterUserDto());
        return "RegisterView";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") RegisterUserDto registerUserDto,
                                 BindingResult bindingResult,
                                 HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "RegisterView";
        }

        try {
            User user = userMapper.registerDTOToObject(registerUserDto);
            userService.create(user);
            return "redirect:/auth/login";
        } catch (EntityDuplicateException e) {
            System.out.println("error here");
            bindingResult.rejectValue("username", "username_error", e.getMessage());
            return "RegisterView";
        }
    }
}
