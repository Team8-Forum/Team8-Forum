package com.example.team8forum.models;

import jakarta.persistence.*;


@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int id;

    @Column(name = "comment")
    private String comment;

    @Column(name = "post_id")
    private int postId;

    @Column(name = "user_id")
    private int userId;

    public Comment() {
    }

    public Comment(String comment, int postId, int userId) {
        this.comment = comment;
        this.postId = postId;
        this.userId = userId;
    }

    public int getCommentId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


}

