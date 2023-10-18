package com.example.team8forum.repositories.contracts;

import com.example.team8forum.models.FilterOptions;
import com.example.team8forum.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PostRepository {
    List<Post> get(FilterOptions filterOptions);

    Post get(int id);

    Post get(String name);

    void create(Post beer);

    void update(Post beer);

    void delete(Post id);


}
