package com.example.team8forum.helpers;

import com.example.team8forum.models.Comment;
import com.example.team8forum.models.dtos.CommentDto;
import com.example.team8forum.services.contracts.CommentService;
import com.example.team8forum.services.contracts.PostService;
import com.example.team8forum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    private final CommentService commentService;

    private final PostService postService;

    private final UserService userService;

    @Autowired
    public CommentMapper(CommentService commentService, PostService postService, UserService userService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    public Comment fromDto(int id, CommentDto dto) {
        Comment comment = fromDto(dto);
        comment.setCommentId(id);
        Comment repositoryComment = commentService.getCommentById(id);
        comment.setCreatedBy(repositoryComment.getCreatedBy());
        comment.setPost(repositoryComment.getPost());
       // comment.setCreatedBy();
        return comment;
    }

    public Comment fromDto(CommentDto dto){
        Comment comment = new Comment();
        comment.setComment(dto.getContent());
        return comment;
    }

}
