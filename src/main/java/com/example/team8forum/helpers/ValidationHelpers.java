package com.example.team8forum.helpers;

import com.example.team8forum.exceptions.AuthorizationException;
import com.example.team8forum.models.Comment;
import com.example.team8forum.models.Post;
import com.example.team8forum.models.User;

public class ValidationHelpers {

    private static final String UNAUTHORIZED_POST_UPDATE = "Ony admin or post's creator can modify a post.";
    private static final String BLOCKED_USER_ERROR_MESSAGE = "Your account is currently blocked! You cannot create or edit posts.";
    private static final String MODIFY_USER_ERROR_MESSAGE = "Only admin or account's owners can modify a user";

    public static void validateUserIsAdmin(User user){
        if(!user.isAdmin()){
            throw new AuthorizationException(UNAUTHORIZED_POST_UPDATE);
        }
    }

    public static void validateUserIsAdminOrPostCreator(User user, Post post){
        if(!user.isAdmin()){
            if(!post.getCreatedBy().getUsername().equals(user.getUsername())){
                throw new AuthorizationException(UNAUTHORIZED_POST_UPDATE);
            }
        }
    }

    public static void validateUserIsAdminOrCommentCreator(User user, Comment comment){
        if(!user.isAdmin()){
            if(!comment.getCreatedBy().getUsername().equals(user.getUsername())){
                throw new AuthorizationException(UNAUTHORIZED_POST_UPDATE);
            }
        }
    }

    public static void validateUserIsAdminOrAccountOwner(User executeUser, User userToUpdate){
        if(!executeUser.isAdmin()){
            if(!executeUser.getUsername().equalsIgnoreCase(userToUpdate.getUsername())){
                throw new AuthorizationException(MODIFY_USER_ERROR_MESSAGE);
            }
        }
    }

    public static void validateUserIsBlocked(User user) {
        if (user.isBlocked()) {
            throw new AuthorizationException(BLOCKED_USER_ERROR_MESSAGE);
        }
    }
}
