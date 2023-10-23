package com.example.team8forum.repositories.contracts;

import com.example.team8forum.models.FilterOptions;
import com.example.team8forum.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PostRepository {
    List<Post> getAll(FilterOptions filterOptions);

    Post get(int id);

    Post get(String name);

    List<Post> getTenMostCommentedPosts();

    List<Post> getTenMostRecent();

    void create(Post post);

    void update(Post post);

    void delete(int id);


}
