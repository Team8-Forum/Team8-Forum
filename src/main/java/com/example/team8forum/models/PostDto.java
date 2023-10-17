package com.example.team8forum.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PostDto {
    @NotNull(message = "The post must be created by a registered user.")
    private User createdBy;

    @NotNull(message = "The post must have a title.")
    @Size(min = 16, max = 64, message = "The title must be between 16 and 64 symbols")
    private String title;

    @NotNull(message = "The post must have a content.")
    @Size(min = 4, max = 8192, message = "The content must be between 32 symbols and 8192 symbols.")
    private String content;

    public PostDto() {
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
}