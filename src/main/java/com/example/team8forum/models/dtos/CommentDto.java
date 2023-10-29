package com.example.team8forum.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class CommentDto {
      //  @NotNull
      //  private int postId;
        @NotBlank(message = "You may not leave a blank comment.")
        private String content;
      //  @NotBlank
      //  private String username;

        public CommentDto() {
        }
        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

      //  public int getPostId() { return postId; }

    //    public void setPostId(int postId) { this.postId = postId; }

     //   public String getUsername() { return username; }

     //   public void setUsername(String userName) { this.username = userName; }
}

