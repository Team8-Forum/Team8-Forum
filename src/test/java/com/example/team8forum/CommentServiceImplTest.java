package com.example.team8forum;

import com.example.team8forum.exceptions.AuthorizationException;
import com.example.team8forum.models.Comment;
import com.example.team8forum.models.User;
import com.example.team8forum.repositories.contracts.CommentRepository;
import com.example.team8forum.services.CommentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.team8forum.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {
    @InjectMocks
    private CommentServiceImpl service;

    @Mock
    private CommentRepository mockRepository;

    @Test
    public void findCommentById_Should_ReturnComment_When_MatchByIdExist() {
        Comment mockComment = createMockComment();

        Mockito.when(mockRepository.findCommentById(Mockito.anyInt()))
                .thenReturn(mockComment);

        // Act
        Comment result = mockRepository.findCommentById(mockComment.getCommentId());

        // Assert
        Assertions.assertEquals(mockComment, result);
    }

    @Test
    void update_Should_CallRepository_When_UserIsAdmin() {
        // Arrange
        User mockUserAdmin = createMockAdmin();
        Comment mockComment = createMockComment();

        // Act
        service.update(mockComment, mockUserAdmin);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockComment);
    }

    @Test
    public void update_Should_CallRepository_When_UpdatingExistingComment() {
        // Arrange
        Comment mockBeer = createMockComment();
        User mockUserCreator = mockBeer.getCreatedBy();

        // Act
        service.update(mockBeer, mockUserCreator);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockBeer);
    }

    @Test
    public void update_Should_ThrowException_When_UserIsNotCreatorOrAdmin() {
        // Arrange
        Comment mockComment = createMockComment();

        // Act, Assert
        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.update(mockComment, Mockito.mock(User.class)));
    }

    @Test
    void delete_Should_CallRepository_When_UserIsCreator() {
        // Arrange
        Comment mockComment = createMockComment();
        User mockUserCreator = mockComment.getCreatedBy();

        // Act
        service.delete(1, mockUserCreator);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(1);
    }

    @Test
    void delete_Should_ThrowException_When_UserIsNotAdminOrCreator() {
        // Arrange
        Comment mockComment = createMockComment();

        Mockito.when(mockRepository.findCommentById(Mockito.anyInt()))
                .thenReturn(mockComment);

        User mockUser = createMockUserWithId2();

        // Act, Assert
        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.delete(1, mockUser));
    }


    @SpringBootTest
    static
    class Team8ForumApplicationTests {

        @Test
        void contextLoads() {
        }

    }
}
