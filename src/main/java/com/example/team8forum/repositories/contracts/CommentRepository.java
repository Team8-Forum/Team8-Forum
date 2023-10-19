package com.example.team8forum.repositories.contracts;

import com.example.team8forum.models.Comment;

import java.util.List;
import java.util.Set;

public interface CommentRepository {


    Set<Comment> findCommentsByPostId(int postId);

    Comment findCommentById(int id);

    Comment create(Comment comment);

    void update(Comment comment);

    void delete(Comment comment);

}
