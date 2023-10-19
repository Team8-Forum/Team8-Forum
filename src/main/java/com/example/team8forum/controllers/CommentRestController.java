package com.example.team8forum.controllers;

import com.example.team8forum.models.Comment;
import com.example.team8forum.models.User;
import com.example.team8forum.models.dtos.CommentDto;
import com.example.team8forum.services.contracts.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    private final CommentService commentService;
    @Autowired
    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public Comment create (@RequestBody CommentDto commentDto, User user){
        Comment comment = commentService.create(commentDto, user);
        return comment;
    }

    @GetMapping("")
    public Set<Comment> getCommentsByPostId(@RequestParam int postId) {
        Set<Comment> comments = commentService.get(postId);
        return comments;
    }

    @PutMapping("/{commentId}")
    public Comment updateComment(@RequestBody CommentDto commentDto, User user) {
        Comment comment = commentService.create(commentDto, user);
        return comment;
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable int commentId, @RequestParam User user) {
        commentService.delete(commentId, user);
    }
}
