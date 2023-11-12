package com.example.team8forum.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;

public class CommentDto {
        @NotEmpty
        @Size(min = 32, max = 8192, message = "Content should be between 32 and 8192 symbols")
        private String content;

        public CommentDto() {
        }
        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
}

