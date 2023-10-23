package com.example.team8forum.controllers;

import com.example.team8forum.exceptions.AuthorizationException;
import com.example.team8forum.exceptions.EntityNotFoundException;
import com.example.team8forum.helpers.AuthenticationHelper;
import com.example.team8forum.helpers.CommentMapper;
import com.example.team8forum.models.Comment;
import com.example.team8forum.models.User;
import com.example.team8forum.services.contracts.CommentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import static com.example.team8forum.Helpers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {


    @MockBean
    CommentService mockService;

    @MockBean
    CommentMapper mockCommentMapper;

    @MockBean
    AuthenticationHelper mockAuthenticationHelper;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void create_Should_ReturnStatusOk_When_CorrectRequest() throws Exception {
        // Arrange
        User mockUser = createMockUser();

        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenReturn(mockUser);

        Comment mockComment = createMockComment();

        Mockito.when(mockCommentMapper.fromDto(Mockito.any()))
                .thenReturn(mockComment);

        // Act, Assert
        String body = toJson(createCommentDto());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void create_Should_ReturnStatusUnauthorized_When_AuthorizationIsMissing() throws Exception {
        // Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, null));

        // Act, Assert
        String body = toJson(createCommentDto());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void create_Should_ReturnStatusNotFound_WhenPostDoesntExist() throws Exception {
        // Arrange
        User mockUser = createMockUser();

        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenReturn(mockUser);

        Mockito.when(mockCommentMapper.fromDto(Mockito.any()))
                .thenThrow(EntityNotFoundException.class);

        // Act, Assert
        String body = toJson(createCommentDto());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void delete_Should_ReturnStatusOk_When_CorrectRequest() throws Exception {
        // Arrange
        User mockUser = createMockUser();

        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenReturn(mockUser);

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/comments/{commentId}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void delete_Should_ReturnStatusUnauthorized_When_AuthorizationIsMissing() throws Exception {
        // Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, null));

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/comments/{commentId}", 1))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void delete_Should_ReturnStatusNotFound_When_BeerDoesNotExist() throws Exception {
        // Arrange
        User mockUser = createMockUser();

        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenReturn(mockUser);

        Mockito.doThrow(EntityNotFoundException.class)
                .when(mockService)
                .delete(1, mockUser);

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/comments/{commentId}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void delete_Should_ReturnStatusUnauthorized_When_UserIsNotAuthorizedToEdit() throws Exception {
        // Arrange
        User mockUser = createMockUser();

        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenReturn(mockUser);

        Mockito.doThrow(AuthorizationException.class)
                .when(mockService)
                .delete(1, mockUser);

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/comments/{commentId}", 1))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

}
