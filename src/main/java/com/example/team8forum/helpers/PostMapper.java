package com.example.team8forum.helpers;

import com.example.team8forum.models.Post;
import com.example.team8forum.models.User;
import com.example.team8forum.models.dtos.PostCreateDto;
import com.example.team8forum.models.dtos.PostDto;
import com.example.team8forum.services.contracts.PostService;
import com.example.team8forum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;

@Component
public class PostMapper {
    private final PostService postService;
    private final UserService userService;
    @Autowired
    public PostMapper(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    public Post fromDto(int id, PostDto dto) {
        Post postToBeUpdated = postService.get(id);
        postToBeUpdated.setTitle(dto.getTitle());
        postToBeUpdated.setContent(dto.getContent());
        return postToBeUpdated;
    }

    public Post fromDto(PostDto dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        return post;
    }

    public PostDto toDto(Post post) {
        PostDto dto = new PostDto();
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
       // dto.setComments(post.getComments());
        return dto;
    }

    public Post mvcDtoToObject(PostCreateDto postCreateDto, User user) {
            Post post = new Post();
            post.setTitle(postCreateDto.getTitle());
            post.setContent(postCreateDto.getContent());
            post.setCreatedBy(user);
            post.setLikes(new HashSet<>());
            post.setComments(new HashSet<>());
            return post;
    }
}
