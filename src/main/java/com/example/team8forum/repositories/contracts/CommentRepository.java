package com.example.team8forum.repositories.contracts;

import com.example.team8forum.models.Comment;

import java.util.List;

public interface CommentRepository {

   // List<Comment> get(int postId);

    List<Comment> findCommentsByPostId(int postId);

    Comment findCommentById(int id);

    void create(Comment comment);

    void update(Comment comment);

    void delete(Comment comment);

}
