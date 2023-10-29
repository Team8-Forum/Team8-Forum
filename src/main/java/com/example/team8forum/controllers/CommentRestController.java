package com.example.team8forum.controllers;

import com.example.team8forum.exceptions.AuthorizationException;
import com.example.team8forum.exceptions.EntityDuplicateException;
import com.example.team8forum.exceptions.EntityNotFoundException;
import com.example.team8forum.helpers.AuthenticationHelper;
import com.example.team8forum.helpers.CommentMapper;
import com.example.team8forum.models.Comment;
import com.example.team8forum.models.Post;
import com.example.team8forum.models.User;
import com.example.team8forum.models.dtos.CommentDto;
import com.example.team8forum.services.contracts.CommentService;
import com.example.team8forum.services.contracts.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    private final CommentService commentService;

    private final PostService postService;

    private final CommentMapper commentMapper;

    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public CommentRestController(CommentService commentService, PostService postService, CommentMapper commentMapper,
                                 AuthenticationHelper authenticationHelper) {
        this.commentService = commentService;
        this.postService = postService;
        this.commentMapper = commentMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @PostMapping("/post/{postId}")
    public Comment create (@RequestHeader HttpHeaders headers,
                           @Valid @RequestBody CommentDto commentDto, @PathVariable int postId){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Comment comment = commentMapper.fromDto(commentDto);
            Post post = postService.get(postId);
            commentService.create(comment, user, post);
            return comment;
    } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("")
    public Set<Comment> getCommentsByPostId(@RequestParam int postId) {
        Set<Comment> comments = commentService.get(postId);
        return comments;
    }

    @PutMapping("/{commentId}")
    public void updateComment(@RequestHeader HttpHeaders headers, @PathVariable int commentId,
            @RequestBody CommentDto commentDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
           // Comment comment = commentMapper.fromDto(commentId, commentDto);
            commentService.update(commentId, commentDto.getContent(), user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@RequestHeader HttpHeaders headers, @PathVariable int commentId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            commentService.delete(commentId,user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

}
