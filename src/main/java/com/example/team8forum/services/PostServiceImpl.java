package com.example.team8forum.services;

import com.example.team8forum.exceptions.EntityDuplicateException;
import com.example.team8forum.exceptions.EntityNotFoundException;
import com.example.team8forum.helpers.ValidationHelpers;
import com.example.team8forum.models.FilterOptions;
import com.example.team8forum.models.Post;
import com.example.team8forum.models.User;
import com.example.team8forum.repositories.PostRepositoryImpl;
import com.example.team8forum.services.contracts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.team8forum.helpers.ValidationHelpers.validateUserIsBlocked;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepositoryImpl repository;
    @Autowired
    public PostServiceImpl(PostRepositoryImpl postRepository) {
        this.repository = postRepository;
    }

    @Override
    public List<Post> get(FilterOptions filterOptions) {
        return repository.getAll(filterOptions);
    }

    @Override
    public Post get(int id) {
        return repository.get(id);
    }

    @Override
    public List<Post> getMostCommentedPosts() {
        return repository.getTenMostCommentedPosts();
    }

    @Override
    public List<Post> getTenMostRecent() {
        return repository.getTenMostRecent();
    }

    @Override
    public void create(Post post, User user) {
        boolean duplicateExists = true;
        try {
            repository.get(post.getTitle());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Post", "title", post.getTitle());
        }

        post.setCreatedBy(user);
        repository.create(post);
    }

    @Override
    public Post likePost(User user, int postId) {
        validateUserIsBlocked(user);
        Post post = get(postId);
        boolean voted = post.getLikes().contains(user);
        if (voted) {
            post.getLikes().remove(user);
        } else {
            post.getLikes().add(user);
        }
        repository.update(post);
        return post;
    }

    @Override
    public void update(Post post, User user) {
        checkModifyPermissions(post.getId(), user);

        boolean duplicateExists = true;
        try {
            Post existingPost = repository.get(post.getTitle());
            if (existingPost.getId() == post.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Post", "title", post.getTitle());
        }

        repository.update(post);
    }

    @Override
    public void delete(int id, User user) {
        checkModifyPermissions(id, user);
        repository.delete(id);

    }
    private void checkModifyPermissions(int postId, User user) {
        Post post = repository.get(postId);
        ValidationHelpers.validateUserIsAdminOrPostCreator(user, post);
    }
}
