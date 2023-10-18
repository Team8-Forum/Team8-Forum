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
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;

    public Comment() {
    }

    public Comment(String comment, Post post, User user) {
        this.comment = comment;
        this.post = post;
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

    public Post getPostId() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }


}

