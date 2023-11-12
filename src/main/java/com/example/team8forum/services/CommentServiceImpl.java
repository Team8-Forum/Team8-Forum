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

import java.util.List;
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
    public List<Comment> get(int postId) {
        return commentRepository.findCommentsByPostId(postId);
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
    public void updateComment(Comment comment, User user) {
        validateUserIsBlocked(user);
        validateUserIsDeleted(user);
        validateUserIsAdminOrCommentCreator(user, comment);
        commentRepository.update(comment);
    }

    @Override
    public void deleteComment(int commentId, User user) {
        Comment comment = getCommentById(commentId);
        validateUserIsBlocked(user);
        validateUserIsDeleted(user);
        validateUserIsAdminOrCommentCreator(user, comment);
        commentRepository.delete(comment);
    }

}
