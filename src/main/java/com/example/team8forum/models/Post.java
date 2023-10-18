package com.example.team8forum.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.data.annotation.Persistent;

import java.util.*;


@Entity
@Table(name = "posts")

public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private int id;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;
    @Column(name = "likes")
    private int likes;

    @OneToMany
    @JoinColumn(name = "post_id")
    private List<Comment> comments;

    @Column(name = "creation_date")
    private Date creationDate;
    public Post() {
    }

    public Post(User createdBy, String title, String content) {
        this.createdBy = createdBy;
        this.title = title;
        this.content = content;
        this.likes = 0;
        this.comments = new ArrayList<>();
        this.creationDate = new Date();
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id && likes == post.likes && Objects.equals(createdBy, post.createdBy) && Objects.equals(title, post.title) && Objects.equals(content, post.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdBy, title, content, likes);
    }
}
