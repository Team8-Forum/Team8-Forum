package com.example.team8forum;

import com.example.team8forum.models.Comment;
import com.example.team8forum.models.PhoneNumber;
import com.example.team8forum.models.Post;
import com.example.team8forum.models.User;
import com.example.team8forum.models.dtos.CommentDto;
import com.fasterxml.jackson.databind.ObjectMapper;



public class Helpers {

    public static User createMockAdmin() {
        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        return mockUser;
    }

    public static User createMockBlockedUser() {
        User mockUser = createMockUser();
        mockUser.setBlocked(true);
        return mockUser;
    }

    public static User createMockUser() {
        var mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("MockUsername");
        mockUser.setPassword("MockPassword");
        mockUser.setLastName("MockLastName");
        mockUser.setFirstName("MockFirstName");
        mockUser.setEmail("mock@user.com");
        mockUser.setAdmin(true);
        mockUser.setBlocked(false);
        return mockUser;
    }

    public static User createMockUserWithId2() {
        var mockUser = new User();
        mockUser.setId(2);
        mockUser.setUsername("MockUsername");
        mockUser.setPassword("MockPassword");
        mockUser.setLastName("MockLastName");
        mockUser.setFirstName("MockFirstName");
        mockUser.setEmail("mock@user.com");
        mockUser.setAdmin(false);
        mockUser.setBlocked(false);
        return mockUser;
    }

    public static Post createMockPost() {
        var mockPost = new Post();
        mockPost.setId(1);
        mockPost.setCreatedBy(createMockUser());
        mockPost.setTitle("Mock Title");
        mockPost.setContent("Mock Content");
        return mockPost;
    }


    public static Comment createMockComment() {
        var mockComment= new Comment();
        mockComment.setCommentId(1);
        mockComment.setPost(createMockPost());
        mockComment.setComment("Mock Comment");
        mockComment.setCreatedBy(createMockUser());
        return mockComment;
    }


    public static CommentDto createCommentDto() {
        CommentDto dto = new CommentDto();
        dto.setContent("Mock content");
        dto.setUsername(createMockUser().getUsername());
        dto.setPostId(createMockPost().getId());
        return dto;
    }

    public static PhoneNumber createMockPhoneNumber() {
        var mockPhone = new PhoneNumber();
        mockPhone.setPhoneNumber("2312323113");
        return mockPhone;
    }

    public static String toJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
