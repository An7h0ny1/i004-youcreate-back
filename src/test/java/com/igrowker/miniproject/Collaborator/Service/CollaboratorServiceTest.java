package com.igrowker.miniproject.Collaborator.Service;

import com.igrowker.miniproject.Collaborator.DTO.CollaboratorCreateRequestDTO;
import com.igrowker.miniproject.Collaborator.DTO.CollaboratorEntityResponseDTO;
import com.igrowker.miniproject.Collaborator.DTO.CollaboratorUpdateRequestDTO;
import com.igrowker.miniproject.Collaborator.Exception.CollaboratorNotFoundException;
import com.igrowker.miniproject.Collaborator.Model.Collaborator;
import com.igrowker.miniproject.Collaborator.Repository.CollaboratorRepository;
import com.igrowker.miniproject.User.Exception.UserNotFoundException;
import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CollaboratorServiceTest {

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CollaboratorService collaboratorService;

    @Test
    @DisplayName("Test getCollaborators method")
    void testGetCollaborators_UserExists_ReturnsCollaborators() {
        // Given
        Long userId = 1L;

        UserEntity user = new UserEntity();
        user.setId(userId);

        Collaborator collaborator1 = Collaborator.builder()
                .id(1L)
                .name("John Doe")
                .service("Cleaning")
                .date("2021-09-01")
                .amount(200.0)
                .user(user)
                .build();

        Collaborator collaborator2 = Collaborator.builder()
                .id(2L)
                .name("Jane Smith")
                .service("Cooking")
                .date("2021-09-01")
                .amount(150.0)
                .user(user)
                .build();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(collaboratorRepository.findByUserId(userId)).thenReturn(List.of(collaborator1, collaborator2));

        // When
        List<CollaboratorEntityResponseDTO> result = collaboratorService.getCollaborators(userId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).name());
        assertEquals("Cleaning", result.get(0).service());
        assertEquals(200.0, result.get(0).amount());
        assertEquals("2021-09-01", result.get(0).date());
        assertEquals("Jane Smith", result.get(1).name());
        assertEquals("Cooking", result.get(1).service());
        assertEquals(150.0, result.get(1).amount());
        assertEquals("2021-09-01", result.get(1).date());
    }

    @Test
    @DisplayName("Test getCollaborators method with user not found")
    void testGetCollaborators_UserDoesNotExist_ThrowsException() {
        // When
        Long userId = 1L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Then
        assertThrows(UserNotFoundException.class, () -> collaboratorService.getCollaborators(userId));
    }

    @Test
    @DisplayName("Test getCollaborator method")
    void testGetCollaborator_CollaboratorExists_ReturnsCollaborator() {
        // Given
        Long collaboratorId = 1L;

        Collaborator collaborator = Collaborator.builder()
                .id(collaboratorId)
                .name("John Doe")
                .service("Cleaning")
                .date("2021-09-01")
                .amount(200.0)
                .user(UserEntity.builder().id(1L).build())
                .build();

        Mockito.when(collaboratorRepository.findById(collaboratorId)).thenReturn(Optional.of(collaborator));

        // When
        CollaboratorEntityResponseDTO result = collaboratorService.getCollaborator(collaboratorId);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.name());
        assertEquals("Cleaning", result.service());
        assertEquals("2021-09-01", result.date());
        assertEquals(200.0, result.amount());
    }

    @Test
    @DisplayName("Test getCollaborator method with collaborator not found")
    void testGetCollaborator_CollaboratorDoesNotExist_ThrowsException() {
        // When
        Long collaboratorId = 1L;

        Mockito.when(collaboratorRepository.findById(collaboratorId)).thenReturn(Optional.empty());

        // Then
        assertThrows(CollaboratorNotFoundException.class, () -> collaboratorService.getCollaborator(collaboratorId));
    }

    @Test
    @DisplayName("Test createCollaborator method")
    void testCreateCollaborator_Success() {
        // Given
        Long userId = 1L;

        UserEntity user = new UserEntity();
        user.setId(userId);

        Collaborator collaborator = Collaborator.builder()
                .id(1L)
                .name("John Doe")
                .service("Cleaning")
                .amount(200.0)
                .date("2021-09-01")
                .user(user)
                .build();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(collaboratorRepository.save(Mockito.any(Collaborator.class))).thenReturn(collaborator);

        CollaboratorCreateRequestDTO collaboratorCreateRequestDTO = CollaboratorCreateRequestDTO.builder()
                .name("John Doe")
                .service("Cleaning")
                .amount(200.0)
                .date("2021-09-01")
                .user_id(userId)
                .build();
        // When
        CollaboratorEntityResponseDTO result = collaboratorService.createCollaborator(collaboratorCreateRequestDTO);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.name());
        assertEquals("Cleaning", result.service());
        assertEquals("2021-09-01", result.date());
        assertEquals(200.0, result.amount());
    }

    @Test
    @DisplayName("Test createCollaborator method with user not found")
    void testCreateCollaborator_UserDoesNotExist_ThrowsException() {
        // Given
        Collaborator collaborator = Collaborator.builder()
                .id(1L)
                .name("John Doe")
                .service("Cleaning")
                .amount(200.0)
                .date("2021-09-01")
                .user(UserEntity.builder().id(1L).build())
                .build();

        Mockito.when(userRepository.findById(collaborator.getUser().getId())).thenReturn(Optional.empty());
        CollaboratorCreateRequestDTO collaboratorCreateRequestDTO = CollaboratorCreateRequestDTO.builder()
                .name("John Doe")
                .service("Cleaning")
                .amount(200.0)
                .date("2021-09-01")
                .user_id(1L)
                .build();

        // Then
        assertThrows(UserNotFoundException.class, () -> collaboratorService.createCollaborator(collaboratorCreateRequestDTO));
    }

    @Test
    @DisplayName("Test updateCollaborator method")
    void testUpdateCollaborator_Success() {
        // Given
        Long collaboratorId = 1L;

        Collaborator collaborator = Collaborator.builder()
                .id(collaboratorId)
                .name("John Doe")
                .service("Cleaning")
                .amount(200.0)
                .date("2021-09-01")
                .user(UserEntity.builder().id(1L).build())
                .build();

        Collaborator collaboratorUpdate = Collaborator.builder()
                .id(collaboratorId)
                .name("Jane Smith")
                .service("Cooking")
                .amount(150.0)
                .date("2021-09-01")
                .user(UserEntity.builder().id(1L).build())
                .build();

        Mockito.when(collaboratorRepository.findById(collaboratorId)).thenReturn(Optional.of(collaborator));
        Mockito.when(collaboratorRepository.save(Mockito.any(Collaborator.class))).thenReturn(collaboratorUpdate);

        CollaboratorUpdateRequestDTO collaboratorUpdateRequestDTO = CollaboratorUpdateRequestDTO.builder()
                .name("Jane Smith")
                .service("Cooking")
                .date("2021-09-01")
                .amount(150.0)
                .build();
        // When
        CollaboratorEntityResponseDTO result = collaboratorService.updateCollaborator(collaboratorId, collaboratorUpdateRequestDTO);


        // Then
        assertNotNull(result);
        assertEquals("Jane Smith", result.name());
        assertEquals("Cooking", result.service());
        assertEquals("2021-09-01", result.date());
        assertEquals(150.0, result.amount());
    }

    @Test
    @DisplayName("Test updateCollaborator method with collaborator not found")
    void testUpdateCollaborator_CollaboratorDoesNotExist_ThrowsException() {
        // Given
        Long collaboratorId = 1L;

        Collaborator collaboratorUpdate = Collaborator.builder()
                .id(collaboratorId)
                .name("Jane Smith")
                .service("Cooking")
                .date("2021-09-01")
                .amount(150.0)
                .user(UserEntity.builder().id(1L).build())
                .build();

        Mockito.when(collaboratorRepository.findById(collaboratorId)).thenReturn(Optional.empty());
        CollaboratorUpdateRequestDTO collaboratorUpdateRequestDTO = CollaboratorUpdateRequestDTO.builder()
                .name("Jane Smith")
                .service("Cooking")
                .date("2021-09-01")
                .amount(150.0)
                .build();

        // Then
        assertThrows(CollaboratorNotFoundException.class, () -> collaboratorService.updateCollaborator(collaboratorId, collaboratorUpdateRequestDTO));
    }

    @Test
    @DisplayName("Test deleteCollaborator method")
    void testDeleteCollaborator_Success() {
        // Given
        Long collaboratorId = 1L;

        Collaborator collaborator = Collaborator.builder()
                .id(collaboratorId)
                .name("John Doe")
                .service("Cleaning")
                .date("2021-09-01")
                .amount(200.0)
                .user(UserEntity.builder().id(1L).build())
                .build();

        Mockito.when(collaboratorRepository.findById(collaboratorId)).thenReturn(Optional.of(collaborator));

        // When
        collaboratorService.deleteCollaborator(collaboratorId);

        // Then
        Mockito.verify(collaboratorRepository).delete(collaborator);
    }

    @Test
    @DisplayName("Test deleteCollaborator method with collaborator not found")
    void testDeleteCollaborator_CollaboratorDoesNotExist_ThrowsException() {
        // Given
        Long collaboratorId = 1L;

        Mockito.when(collaboratorRepository.findById(collaboratorId)).thenReturn(Optional.empty());

        // Then
        assertThrows(CollaboratorNotFoundException.class, () -> collaboratorService.deleteCollaborator(collaboratorId));
    }

    @Test
    @DisplayName("Test entityToDTO method")
    void testEntityToDTO_Success() {
        // Given
        Collaborator collaborator = Collaborator.builder()
                .id(1L)
                .name("John Doe")
                .service("Cleaning")
                .date("2021-09-01")
                .amount(200.0)
                .user(UserEntity.builder().id(1L).build())
                .build();

        // When
        CollaboratorEntityResponseDTO result = collaboratorService.entityToDTO(collaborator);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.name());
        assertEquals("Cleaning", result.service());
        assertEquals("2021-09-01", result.date());
        assertEquals(200.0, result.amount());
    }
}

