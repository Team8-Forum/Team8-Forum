package com.example.team8forum.helpers;

import com.example.team8forum.models.Comment;
import com.example.team8forum.models.Post;
import com.example.team8forum.models.User;
import com.example.team8forum.models.dtos.CommentDto;
import com.example.team8forum.models.dtos.CommentOutDto;
import com.example.team8forum.services.contracts.CommentService;
import com.example.team8forum.services.contracts.PostService;
import com.example.team8forum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;

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
        Comment comment = commentService.getCommentById(id);
        comment.setComment(dto.getContent());
        return comment;
    }

    public Comment fromDto(CommentDto dto){
        Comment comment = new Comment();
        comment.setComment(dto.getContent());
        return comment;
    }

    public Comment fromDto(CommentDto commentDTO, int id, User user) {
        Comment comment = new Comment();
        Post post = postService.get(id);
        comment.setPost(post);
        comment.setComment(commentDTO.getContent());
        comment.setCreatedBy(user);
        return comment;
    }

    public CommentOutDto objectToDto(Comment comment) {
        CommentOutDto commentOutDTO = new CommentOutDto();
        commentOutDTO.setId(comment.getCommentId());
        commentOutDTO.setContent(comment.getComment());
        commentOutDTO.setAuthorUsername(comment.getCreatedBy().getUsername());
        return commentOutDTO;
    }

}
