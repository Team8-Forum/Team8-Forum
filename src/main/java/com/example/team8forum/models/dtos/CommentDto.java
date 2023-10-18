package com.example.team8forum.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CommentDto {
        @NotNull
        private int postId;
        @NotBlank(message = "You may not leave a blank comment.")
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

