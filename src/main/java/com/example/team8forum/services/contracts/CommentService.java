package com.example.team8forum.services.contracts;


import com.example.team8forum.models.Comment;
import com.example.team8forum.models.Post;
import com.example.team8forum.models.User;
import com.example.team8forum.models.dtos.CommentDto;


import java.util.List;
import java.util.Set;


public interface CommentService {

    Set<Comment> get(int postId);

    Comment getCommentById(int id);

    Comment create(Comment comment, User user);

    void update(Comment comment, User user);

    void delete(int id, User user);



}
