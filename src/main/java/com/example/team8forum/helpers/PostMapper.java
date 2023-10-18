package com.example.team8forum.helpers;

import com.example.team8forum.models.Post;
import com.example.team8forum.models.dtos.PostDto;
import com.example.team8forum.services.contracts.PostService;
import com.example.team8forum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    private final PostService postService;
    private final UserService userService;
    @Autowired
    public PostMapper(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    private Post fromDto(int id, PostDto dto) {
        Post post = fromDto(dto);
        post.setId(id);
        Post repositoryPost = postService.get(id);
        post.setCreatedBy(repositoryPost.getCreatedBy());
        return post;
    }

    public Post fromDto(PostDto dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCreatedBy(userService.getById(dto.getCreatedBy().getId()));
        return post;
    }
}
