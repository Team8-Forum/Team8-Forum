package com.example.team8forum.models.dtos;

import com.example.team8forum.models.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PostDto {
    @NotNull(message = "The post must have a title.")
    @Size(min = 16, max = 64, message = "The title must be between 16 and 64 symbols")
    private String title;

    @NotNull(message = "The post must have a content.")
    @Size(min = 4, max = 8192, message = "The content must be between 32 symbols and 8192 symbols.")
    private String content;

    public PostDto() {
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
