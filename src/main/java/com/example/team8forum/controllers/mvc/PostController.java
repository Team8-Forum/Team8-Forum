package com.example.team8forum.controllers.mvc;

import com.example.team8forum.exceptions.EntityNotFoundException;
import com.example.team8forum.helpers.PostMapper;
import com.example.team8forum.models.FilterOptions;
import com.example.team8forum.models.Post;
import com.example.team8forum.models.User;
import com.example.team8forum.models.dtos.PostDto;
import com.example.team8forum.services.contracts.PostService;
import com.example.team8forum.services.contracts.UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/posts")
@Controller
public class PostController {
    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, PostMapper postMapper, UserService userService) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.userService = userService;
    }

    @ModelAttribute("requestURI")
    public String getRequestURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping
    public String ShowAllPosts(Model model) {
        FilterOptions filter = new FilterOptions(null, null, null,
                null, null, null);
        List<Post> posts = postService.get(filter);
        model.addAttribute("posts", posts);
        return "PostsView";

    }

    @GetMapping("/{id}")
    public String ShowPost(@PathVariable int id, Model model) {
        try {
            Post post = postService.get(id);
            model.addAttribute("post", post);
            return "PostView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            return "ErrorView";
        }
    }

    @GetMapping("/create")
    public String createPostForm(Model model) {
        model.addAttribute("post", new PostDto());
        return "PostCreateView";
    }

    @PostMapping("/create")
    public String createPost(@Valid @ModelAttribute("post") PostDto postDto,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "PostCreateView";
        }
        try {
            //TODO Get authenticated user from HTTP session
            User user = userService.getById(1);
            Post post = postMapper.fromDto(postDto);
            postService.create(post, user);
            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/update")
    public String updatePostForm(@PathVariable int id, Model model){
        try {
            Post post = postService.get(id);
            PostDto postDto = postMapper.toDto(post);
            model.addAttribute("post", postDto);
            return "PostUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/update")
    public String updatePost(@PathVariable int id,
                             @Valid @ModelAttribute("post") PostDto postDto,
                             BindingResult result,  Model model){
        if (result.hasErrors()) {
            return "PostUpdateView";
        }

        try {
            //TODO Get authenticated user
            User user = userService.getById(1);
            Post post = postMapper.fromDto(id, postDto);
            postService.update(post, user);
            return "redirect:/posts/{id}/update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/delete")
    public String deletePost(@PathVariable int id, Model model) {
        try {
            //TODO Get authenticated user
            User user = userService.getById(1);
            postService.delete(id, user);
            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }



}

