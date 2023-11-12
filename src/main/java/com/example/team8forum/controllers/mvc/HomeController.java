package com.example.team8forum.controllers.mvc;

import com.example.team8forum.helpers.AuthenticationHelper;
import com.example.team8forum.models.Post;
import com.example.team8forum.models.User;
import com.example.team8forum.services.contracts.PostService;
import com.example.team8forum.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/")
@Controller
public class HomeController {
    private final UserService userService;
    private final PostService postService;
    private final AuthenticationHelper authenticationHelper;


    @Autowired
    public HomeController(UserService userService, PostService postService, AuthenticationHelper authenticationHelper) {
        this.postService = postService;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public String showHomePage(Model model, HttpSession session) {
        List<Post> postResults =  postService.getAll();
        List<Post> mostCommentedPosts = postService.getMostCommentedPosts();
        List<Post> mostRecentPosts = postService.getTenMostRecent();
        model.addAttribute("usersSize", userService.getAll().size());
        model.addAttribute("postsSize", postService.getAll().size());
        model.addAttribute("posts", postResults);
        model.addAttribute("mostRecentPosts", mostRecentPosts);
        model.addAttribute("mostCommentedPosts", mostCommentedPosts);
        return "HomeView";
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("getUserId")
    public int populateGetUser(HttpSession session) {
        return authenticationHelper.tryGetUserId(session);
    }
}