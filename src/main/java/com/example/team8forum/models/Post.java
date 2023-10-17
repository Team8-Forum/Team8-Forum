package com.example.team8forum.models;

import java.util.Objects;

public class Post {
    private int id;
    private  User createdBy;
    private String title;
    private String content;

    public Post() {
    }

    public Post(User createdBy, String title, String content) {
        this.createdBy = createdBy;
        this.title = title;
        this.content = content;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(createdBy, post.createdBy) && Objects.equals(title, post.title) && Objects.equals(content, post.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdBy, title, content);
    }
}
