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

    private final CommentMapper commentMapper;

    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public CommentRestController(CommentService commentService, CommentMapper commentMapper,
                                 AuthenticationHelper authenticationHelper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @PostMapping
    public Comment create (@RequestHeader HttpHeaders headers,
            @RequestBody CommentDto commentDto){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Comment comment = commentMapper.fromDto(commentDto);
            commentService.create(comment, user);
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
    public Comment updateComment(@RequestHeader HttpHeaders headers, @PathVariable int id,
            @RequestBody CommentDto commentDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Comment comment = commentMapper.fromDto(id, commentDto);
            commentService.update(comment, user);
            return comment;
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
