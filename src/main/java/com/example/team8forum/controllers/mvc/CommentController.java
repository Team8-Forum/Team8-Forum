package com.example.team8forum.controllers.mvc;

import com.example.team8forum.exceptions.AuthorizationException;
import com.example.team8forum.exceptions.EntityNotFoundException;
import com.example.team8forum.helpers.AuthenticationHelper;
import com.example.team8forum.helpers.CommentMapper;
import com.example.team8forum.models.Comment;
import com.example.team8forum.models.Post;
import com.example.team8forum.models.User;
import com.example.team8forum.models.dtos.CommentDto;
import com.example.team8forum.services.contracts.CommentService;
import com.example.team8forum.services.contracts.PostService;
import com.example.team8forum.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/posts")
@Controller
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final UserService userService;
    private final PostService postService;
    private final AuthenticationHelper authenticationHelper;

    public CommentController(CommentService commentService,
                             CommentMapper commentMapper,
                             UserService userService,
                             PostService postService,
                             AuthenticationHelper authenticationHelper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.userService = userService;
        this.postService = postService;
        this.authenticationHelper = authenticationHelper;
    }

    @PostMapping("/{id}/comments")
    public String createComment(@PathVariable("id") int postId,
                                @Valid @ModelAttribute("comment") CommentDto commentDto,
                                BindingResult result,
                                Model model,
                                HttpSession httpSession) {
        if (result.hasErrors()) {
            return "PostView";
        }
        try {
            User user = authenticationHelper.tryGetCurrentUser(httpSession);
            Comment comment = commentMapper.fromDto(commentDto);
            Post post = postService.get(postId);
            commentService.create(comment, user, post);
            return "redirect:/posts/" + postId;
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            return "ErrorView";
        } catch (AuthorizationException e){
            return "redirect:/auth/login";
        }
    }



}
