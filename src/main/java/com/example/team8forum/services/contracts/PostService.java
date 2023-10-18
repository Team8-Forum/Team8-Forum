package com.example.team8forum.services.contracts;

import com.example.team8forum.models.FilterOptions;
import com.example.team8forum.models.Post;
import com.example.team8forum.models.User;

import java.util.List;

public interface PostService {
    List<Post> get(FilterOptions filterOptions);

    Post get(int id);

    void create(Post beer, User user);

    void update(Post beer, User user);

    void delete(Post postToDelete, User user);
}
