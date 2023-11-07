package com.example.team8forum.controllers.mvc;

import com.example.team8forum.exceptions.EntityNotFoundException;
import com.example.team8forum.models.FilterOptions;
import com.example.team8forum.models.Post;
import com.example.team8forum.services.contracts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/posts")
@Controller
public class PostController {
    private final PostService postService;
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }
    @GetMapping
    public String ShowAllPosts(Model model){
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
        } catch (EntityNotFoundException e){
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statusCode",404);
            return "ErrorView";
        }

    }
}
