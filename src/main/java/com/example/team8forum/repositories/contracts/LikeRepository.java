package com.example.team8forum.repositories.contracts;

import com.example.team8forum.models.Like;

import java.util.List;

public interface LikeRepository {
    List<Like> get(int userId, int postId);
    List<Like> get(int postId);
}
