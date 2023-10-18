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

    @ManyToOne
    @Column(name = "user_id")
    private User createdBy;

    public Comment() {
    }

    public Comment(String comment, int postId, User user) {
        this.comment = comment;
        this.postId = postId;
        this.createdBy = user;
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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }


}

