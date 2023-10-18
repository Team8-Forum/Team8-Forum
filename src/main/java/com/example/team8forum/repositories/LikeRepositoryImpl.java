package com.example.team8forum.repositories;

import com.example.team8forum.models.Like;
import com.example.team8forum.repositories.contracts.LikeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LikeRepositoryImpl implements LikeRepository {
    @Override
    public List<Like> get(int userId, int postId) {
        return null;
    }

    @Override
    public List<Like> get(int postId) {
        return null;
    }
}
