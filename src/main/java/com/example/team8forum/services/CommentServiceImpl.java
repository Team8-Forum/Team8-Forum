package com.example.team8forum.services;

import com.example.team8forum.exceptions.AuthorizationException;
import com.example.team8forum.exceptions.EntityNotFoundException;
import com.example.team8forum.models.Comment;

import com.example.team8forum.models.Post;
import com.example.team8forum.models.User;
import com.example.team8forum.repositories.contracts.CommentRepository;
import com.example.team8forum.repositories.contracts.PostRepository;
import com.example.team8forum.services.contracts.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.example.team8forum.helpers.ValidationHelpers.*;

import java.util.Set;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Set<Comment> get(int postId) {
        Set<Comment> comments = commentRepository.findCommentsByPostId(postId);
        return comments;
    }

    @Override
    public Comment getCommentById(int id) {
        return commentRepository.findCommentById(id);
    }


    @Override
    public Comment create(Comment comment, User user, Post post) {
        validateUserIsBlocked(user);
       //Post post = comment.getPost();
        comment.setPost(post);
        comment.setComment(comment.getComment());
        comment.setCreatedBy(user);
        //comment.setCreatedDate(LocalDateTime.now());

        return commentRepository.create(comment);
    }

    @Override
    public void update(int id, String content, User user) {
        Comment comment = commentRepository.findCommentById(id);
        if (comment == null){
            throw new EntityNotFoundException("Comment", "id", String.valueOf(id));
        }
        comment.setComment(content);
        validateUserIsAdminOrCommentCreator(user, comment);
        validateUserIsBlocked(user);
        commentRepository.update(comment);
    }

    @Override
    public void delete(int id, User user) {
        Comment comment = commentRepository.findCommentById(id);
        if (!(user.isAdmin()|| comment.getCreatedBy().equals(user))) {
            throw new AuthorizationException("You may not remove this comment.");
        }
        commentRepository.delete(id);
    }

}
