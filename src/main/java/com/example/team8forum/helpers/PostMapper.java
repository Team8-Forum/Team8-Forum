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

    public Post fromDto(int id, PostDto dto) {
        Post post = fromDto(dto);
        post.setId(id);
        Post repositoryPost = postService.get(id);
        post.setCreatedBy(repositoryPost.getCreatedBy());
        post.setComments(repositoryPost.getComments());
        return post;
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
}
