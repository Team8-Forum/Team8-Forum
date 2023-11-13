package com.example.team8forum.controllers.mvc;

import com.example.team8forum.exceptions.AuthorizationException;
import com.example.team8forum.exceptions.EntityNotFoundException;
import com.example.team8forum.helpers.AuthenticationHelper;
import com.example.team8forum.helpers.CommentMapper;
import com.example.team8forum.helpers.PostMapper;
import com.example.team8forum.models.Comment;
import com.example.team8forum.models.FilterOptions;
import com.example.team8forum.models.Post;
import com.example.team8forum.models.User;
import com.example.team8forum.models.dtos.*;
import com.example.team8forum.services.contracts.CommentService;
import com.example.team8forum.services.contracts.PostService;
import com.example.team8forum.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final CommentMapper commentMapper;
    private final CommentService commentService;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public PostController(PostService postService, PostMapper postMapper, CommentMapper commentMapper,
                          CommentService commentService, UserService userService, AuthenticationHelper authenticationHelper) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.commentMapper = commentMapper;
        this.commentService = commentService;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public String showAllPosts(@ModelAttribute("filterPostDto") FilterPostDto filterPostDto, Model model, HttpSession session) {
        try{
            User user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e){
            return "redirect:/auth/login";
        }
        FilterOptions filterOptions = new FilterOptions(
                filterPostDto.getTitle(),
                null,
                null,
                filterPostDto.getCreationDate(),
                filterPostDto.getSortBy(),
                filterPostDto.getSortOrder());
        List <Post> posts =  postService.get(filterOptions);
        model.addAttribute("posts", posts);
        model.addAttribute("newPost",  new PostCreateDto());
        model.addAttribute("filterPostDto", filterPostDto);
        return "PostsView";
    }

    @GetMapping("/{id}")
    public String showSinglePost(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        try {
            boolean isAdminOrOwner = false;
            Post existingPost = postService.get(id);
            List<Comment> comments = commentService.get(id);
            if(populateIsAuthenticated(session)) {
                user = userService.getByUsername((String) session.getAttribute("currentUser"));
                isAdminOrOwner = authenticationHelper.verifyPostOwnershipOrAdmin(user, existingPost);
            }
            model.addAttribute("user", user);
            model.addAttribute("post", existingPost);
            model.addAttribute("comments", comments);
            model.addAttribute("newPost",  new PostCreateDto());
            model.addAttribute("newComment", new CommentDto());
            model.addAttribute("isOwnerOrAdmin", isAdminOrOwner);
            return "PostView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{postId}/comments")
    public String createCommentInPost(@Valid @ModelAttribute("newComment")CommentDto commentDto, BindingResult errors,
                                      @PathVariable int postId, Model model, HttpSession session){
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        boolean isAdminOrOwner = false;
        Post existingPost = postService.get(postId);
        List<Comment> comments = commentService.get(postId);
        if(populateIsAuthenticated(session)) {
            user = userService.getByUsername((String) session.getAttribute("currentUser"));
            isAdminOrOwner = authenticationHelper.verifyPostOwnershipOrAdmin(user, existingPost);
        }
        if (errors.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("post", existingPost);
            model.addAttribute("comments", comments);
            model.addAttribute("newPost",  new PostCreateDto());
            model.addAttribute("isOwnerOrAdmin", isAdminOrOwner);
            return "PostView";
        }

        try {
            Comment comment = commentMapper.fromDto(commentDto, postId, user);
            commentService.create(comment, user,existingPost);
            return "redirect:/posts/" + postId;
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("user", user);
            model.addAttribute("post", existingPost);
            model.addAttribute("comments", comments);
            model.addAttribute("newPost",  new PostCreateDto());
            model.addAttribute("isOwnerOrAdmin", isAdminOrOwner);
            errors.rejectValue("content", "content_error", e.getMessage());
            return "PostView";
        }
    }

    @PostMapping
    public String createPost(@Valid @ModelAttribute("newPost") PostCreateDto postCreateDto,
                             BindingResult errors, Model model, HttpSession session) {
        try{
            User user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        try {
        User user = authenticationHelper.tryGetCurrentUser(session);

        if (errors.hasErrors()) {
            model.addAttribute("filterPostDto", new FilterPostDto());
            return "PostsView";
        }

            Post post = postMapper.mvcDtoToObject(postCreateDto, user);
            postService.create(post,user);
            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            //errors.rejectValue("content", "content_error", e.getMessage());
            return "redirect:/posts";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditPostPage(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            Post post = postService.get(id);
            model.addAttribute("newPost", post);
            model.addAttribute("postId", id);
            return "PostUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/update")
    public String updatePost(@PathVariable int id,@Valid @ModelAttribute("newPost") PostDto postDto, HttpSession session,
                             BindingResult bindingResult, Model model) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            if (bindingResult.hasErrors()) {
                return "PostUpdateView";
            }
            Post post =  postMapper.fromDto(id,postDto);
            postService.update(post,user);
            return "redirect:/posts/" + post.getId();
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{postId}/comments/{commentId}/update")
    public String showEditCommentPage(@PathVariable int postId, @PathVariable int commentId, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            Comment comment = commentService.getCommentById(commentId);
            CommentOutDto commentOutDTO = commentMapper.objectToDto(comment);
            model.addAttribute("newComment", commentOutDTO);

            if(!user.isAdmin() && !comment.getCreatedBy().getUsername().equals(user.getUsername())) {
                throw new AuthorizationException("You are unauthorized");
            }

            return "CommentUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "NoAccessView";
        }
    }
    @PostMapping("/{postId}/comments/{commentId}/update")
    public String updateComment(@PathVariable int postId, @PathVariable int commentId, @Valid @ModelAttribute("newComment") CommentDto commentDto, HttpSession session,
                                BindingResult bindingResult, Model model) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            if (bindingResult.hasErrors()) {
                return "CommentUpdateView";
            }
            Comment comment = commentMapper.fromDto(commentId, commentDto);
            commentService.updateComment(comment,user);
            return "redirect:/posts/" + postId;
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/delete")
    public String deletePost(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        try {
            Post post = postService.get(id);

            if(!user.isAdmin() && !post.getCreatedBy().getUsername().equals(user.getUsername())) {
                throw new AuthorizationException("You are unauthorized");
            }

            postService.delete(id, user);
            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }  catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "NoAccessView";
        }
    }
    @GetMapping("/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable int postId, @PathVariable int commentId, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        try {
            Comment comment = commentService.getCommentById(commentId);

            if(!user.isAdmin() && !comment.getCreatedBy().getUsername().equals(user.getUsername())) {
                throw new AuthorizationException("You are unauthorized");
            }

            commentService.deleteComment(commentId, user);
            return "redirect:/posts/" + postId;
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }  catch (AuthorizationException e) {
            model.addAttribute("error", e.getMessage());
            return "NoAccessView";
        }
    }
    @PostMapping("/{id}/likes")
    public String likePost(@PathVariable int id, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if(user.isBlocked() || user.isDeleted()) {
            return "redirect:/posts/{id}";
        }

        postService.likePost(user, id);
        return "redirect:/posts/{id}";

    }
    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("getUserId")
    public int populateGetUser(HttpSession session) {
        return authenticationHelper.tryGetUserId(session);
    }

    @ModelAttribute("isLoggedUserABlocked")
    public boolean populateIsLoggedUserBlocked(HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            if(user.isBlocked()) {
                return true;
            }
        } catch (AuthorizationException e) {
            return false;
        }
        return false;
    }
}

