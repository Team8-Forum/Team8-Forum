package com.example.team8forum.services.contracts;

import com.example.team8forum.models.FilterOptions;
import com.example.team8forum.models.Post;
import com.example.team8forum.models.User;

import java.util.List;

public interface PostService {
    List<Post> get(FilterOptions filterOptions);

    Post get(int id);

    List<Post> getMostCommentedPosts();

    List<Post> getTenMostRecent();

    void create(Post post, User user);

    Post likePost(User user, int postId);

    void update(Post beer, User user);

    void delete(int id, User user);
}
