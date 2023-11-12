package com.example.team8forum.services;

import com.example.team8forum.exceptions.EntityDuplicateException;
import com.example.team8forum.exceptions.EntityNotFoundException;
import com.example.team8forum.helpers.ValidationHelpers;
import com.example.team8forum.models.FilterOptions;
import com.example.team8forum.models.Post;
import com.example.team8forum.models.User;
import com.example.team8forum.repositories.CommentRepositoryImpl;
import com.example.team8forum.repositories.PostRepositoryImpl;
import com.example.team8forum.repositories.contracts.CommentRepository;
import com.example.team8forum.repositories.contracts.PostRepository;
import com.example.team8forum.services.contracts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.team8forum.helpers.ValidationHelpers.*;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository repository;
    private final CommentRepository commentRepository;
    @Autowired
    public PostServiceImpl(PostRepository postRepository, CommentRepository commentRepository) {
        this.repository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Post> getAll() {
        return repository.getAll();
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
        return repository.getMostCommentedPosts();
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
        validateUserIsBlocked(user);
        validateUserIsDeleted(user);
        validateUserIsAdminOrPostCreator(user, post);
        repository.update(post);
    }

    @Override
    public void delete(int id, User user) {
        Post post = get(id);
        validateUserIsBlocked(user);
        validateUserIsDeleted(user);
        validateUserIsAdminOrPostCreator(user, post);
        post.getLikes().clear();
        post.getComments().forEach(commentRepository::delete);
        repository.delete(post.getId());

    }
    private void checkModifyPermissions(int postId, User user) {
        Post post = repository.get(postId);
        validateUserIsAdminOrPostCreator(user, post);
    }
}
