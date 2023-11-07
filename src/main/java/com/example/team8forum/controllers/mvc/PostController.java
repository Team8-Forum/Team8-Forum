package com.example.team8forum.controllers.mvc;

import com.example.team8forum.exceptions.EntityNotFoundException;
import com.example.team8forum.helpers.PostMapper;
import com.example.team8forum.models.FilterOptions;
import com.example.team8forum.models.Post;
import com.example.team8forum.models.User;
import com.example.team8forum.models.dtos.PostDto;
import com.example.team8forum.services.contracts.PostService;
import com.example.team8forum.services.contracts.UserService;
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
                             Model model,
                             BindingResult result) {
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


}

