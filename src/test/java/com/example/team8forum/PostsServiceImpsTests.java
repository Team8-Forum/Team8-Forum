package com.example.team8forum;

import com.example.team8forum.exceptions.AuthorizationException;
import com.example.team8forum.exceptions.EntityDuplicateException;
import com.example.team8forum.exceptions.EntityNotFoundException;
import com.example.team8forum.models.FilterOptions;
import com.example.team8forum.models.Post;
import com.example.team8forum.models.User;
import com.example.team8forum.repositories.PostRepositoryImpl;
import com.example.team8forum.repositories.contracts.PostRepository;
import com.example.team8forum.services.PostServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.team8forum.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class PostsServiceImpsTests {
    @Mock
    PostRepositoryImpl mockRepository;

    @InjectMocks
    PostServiceImpl service;

    @Test
    void get_Should_CallRepository(){
        // Arrange
        FilterOptions mockFilterOptions = createMockFilterOptions();
        Mockito.when(mockRepository.getAll(mockFilterOptions))
                .thenReturn(null);

        //Act
        service.get(mockFilterOptions);

        //Assert
        Mockito.verify(mockRepository, Mockito.times(1)).
                getAll(mockFilterOptions);
    }

    @Test
    public void create_Should_CallRepository_When_PostWithSameNameDoesNotExist() {
        // Arrange
        Post mockPost = createMockPost();
        User mockUser = createMockUser();

        Mockito.when(mockRepository.get(mockPost.getTitle()))
                .thenThrow(EntityNotFoundException.class);

        // Act
        service.create(mockPost, mockUser);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(mockPost);
    }

    @Test
    public void create_Should_Throw_When_PostWithSameNameExists() {
        // Arrange
        Post mockPost = createMockPost();
        User mockUser = createMockUser();

        Mockito.when(mockRepository.get(mockPost.getTitle()))
                .thenReturn(mockPost);

        // Act, Assert
        Assertions.assertThrows(
                EntityDuplicateException.class,
                () -> service.create(mockPost, mockUser));
    }

    @Test
    void update_Should_CallRepository_When_UserIsCreator() {
        // Arrange
        Post mockPost = createMockPost();
        User mockUserCreator = mockPost.getCreatedBy();

        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockPost);

        Mockito.when(mockRepository.get(Mockito.anyString()))
                .thenThrow(EntityNotFoundException.class);

        // Act
        service.update(mockPost, mockUserCreator);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockPost);
    }

    @Test
    void update_Should_CallRepository_When_UserIsAdmin() {
        // Arrange
        User mockUserAdmin = createMockAdmin();
        Post mockPost = createMockPost();

        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockPost);

        Mockito.when(mockRepository.get(Mockito.anyString()))
                .thenThrow(EntityNotFoundException.class);

        // Act
        service.update(mockPost, mockUserAdmin);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockPost);
    }

    @Test
    public void update_Should_CallRepository_When_UpdatingExistingPost() {
        // Arrange
        Post mockPost = createMockPost();
        User mockUserCreator = mockPost.getCreatedBy();

        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockPost);

        Mockito.when(mockRepository.get(Mockito.anyString()))
                .thenReturn(mockPost);

        // Act
        service.update(mockPost, mockUserCreator);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockPost);
    }

    @Test
    public void update_Should_ThrowException_When_UserIsNotCreatorOrAdmin() {
        // Arrange
        Post mockPost = createMockPost();

        Mockito.when(mockRepository.get(mockPost.getId()))
                .thenReturn(mockPost);

        // Act, Assert
        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.update(mockPost, Mockito.mock(User.class)));
    }


    @Test
    public void update_Should_ThrowException_When_PostNameIsTaken() {
        // Arrange
        Post mockPost = createMockPost();
        User mockUserCreator = mockPost.getCreatedBy();

        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockPost);

        Post mockExistingBeerWithSameName = createMockPost();
        mockExistingBeerWithSameName.setId(2);

        Mockito.when(mockRepository.get(mockPost.getTitle()))
                .thenReturn(mockExistingBeerWithSameName);

        // Act, Assert
        Assertions.assertThrows(
                EntityDuplicateException.class,
                () -> service.update(mockPost, mockUserCreator));
    }

    @Test
    void delete_Should_CallRepository_When_UserIsCreator() {
        // Arrange
        Post mockBeer = createMockPost();
        User mockUserCreator = mockBeer.getCreatedBy();

        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockBeer);

        // Act
        service.delete(1, mockUserCreator);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(1);
    }

    @Test
    void delete_Should_CallRepository_When_UserIsAdmin() {
        // Arrange
        User mockUserAdmin = createMockAdmin();
        Post mockPost = createMockPost();

        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockPost);

        // Act
        service.delete(1, mockUserAdmin);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(1);
    }

    @Test
    void delete_Should_ThrowException_When_UserIsNotAdminOrCreator() {
        // Arrange
        Post mockPost = createMockPost();

        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockPost);

        User mockUser = Mockito.mock(User.class);

        // Act, Assert
        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.delete(1, mockUser));
    }
}
