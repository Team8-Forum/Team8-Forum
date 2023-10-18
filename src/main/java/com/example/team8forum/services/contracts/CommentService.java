package com.example.team8forum.services.contracts;


import com.example.team8forum.models.Comment;
import com.example.team8forum.models.Post;
import com.example.team8forum.models.User;


import java.util.List;


public interface CommentService {

    List<Comment> get(int postId);

    void create(Comment comment, Post post, User user);

    void update(Comment comment, User user);

    void delete(Comment comment, User user);



}
