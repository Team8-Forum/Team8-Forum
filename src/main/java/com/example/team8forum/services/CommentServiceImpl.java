package com.example.team8forum.services;

import com.example.team8forum.models.Comment;
import com.example.team8forum.models.Post;
import com.example.team8forum.models.User;
import com.example.team8forum.repositories.contracts.CommentRepository;
import com.example.team8forum.services.contracts.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;

    @Autowired
    public CommentServiceImpl(CommentRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Comment> get(int postId) {
        return null;
    }

    @Override
    public void create(Comment comment, Post post, User user) {
        //make sure post exists



    }

    @Override
    public void update(Comment comment, User user) {
        Comment existingComment = repository.findCommentById(comment.getCommentId());
        int currentUserId = user.getId();
        if (currentUserId == user.getId() || user.isAdmin()) {
            repository.update(comment);
        }
    }

    @Override
    public void delete(Comment comment, User user) {
        Comment existingComment = repository.findCommentById(comment.getCommentId());
        int currentUserId = user.getId();
        if (currentUserId == user.getId() || user.isAdmin()) {
            repository.delete(comment);

        }
    }
}
