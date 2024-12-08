package com.igrowker.miniproject.Collaborator.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.igrowker.miniproject.Collaborator.DTO.CollaboratorCreateRequestDTO;
import com.igrowker.miniproject.Collaborator.DTO.CollaboratorEntityResponseDTO;
import com.igrowker.miniproject.Collaborator.DTO.CollaboratorUpdateRequestDTO;
import com.igrowker.miniproject.Collaborator.Service.CollaboratorService;
import com.igrowker.miniproject.Utils.Api_Response;

public class CollaboratorControllerTest {

        @Mock
        private CollaboratorService collaboratorService;

        @InjectMocks
        private CollaboratorController collaboratorController;

        @BeforeEach
        public void setUp() {
                MockitoAnnotations.openMocks(this);
        }

        @Test
        @DisplayName("Test get all collaborators")
        public void testGetCollaborators() {
                Long userId = 1L;
                CollaboratorEntityResponseDTO collaborator = new CollaboratorEntityResponseDTO(
                                1L, "Felipe Bolaños", "Developer", "2024-12-7", 1000D, userId);
                List<CollaboratorEntityResponseDTO> collaborators = Collections
                                .singletonList(collaborator);
                when(collaboratorService.getCollaborators(userId)).thenReturn(collaborators);

                ResponseEntity<Api_Response<List<CollaboratorEntityResponseDTO>>> response = collaboratorController
                                .getCollaborators(userId);

                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals("Colaboradores encontrados", response.getBody().getMessage());
                assertEquals(collaborators, response.getBody().getData());
        }

        @Test
        @DisplayName("Test get collaborator")
        public void testGetCollaborator() {
                Long id = 1L;
                CollaboratorEntityResponseDTO collaborator = new CollaboratorEntityResponseDTO(
                                id, "Felipe Bolaños", "Developer", "2024-12-7", 1000D, 1L);
                when(collaboratorService.getCollaborator(id)).thenReturn(collaborator);

                ResponseEntity<Api_Response<CollaboratorEntityResponseDTO>> response = collaboratorController
                                .getCollaborator(id);

                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals("Colaborador encontrado satisfactoriamente", response.getBody().getMessage());
                assertEquals(collaborator, response.getBody().getData());
        }

        @Test
        @DisplayName("Test create collaborator")
        public void testCreateCollaborator() {
                CollaboratorCreateRequestDTO requestDTO = new CollaboratorCreateRequestDTO();
                CollaboratorEntityResponseDTO collaborator = new CollaboratorEntityResponseDTO(
                                1L, "Felipe Bolaños", "Developer", "2024-12-7", 1000D, 1L);
                when(collaboratorService.createCollaborator(requestDTO)).thenReturn(collaborator);

                ResponseEntity<Api_Response<CollaboratorEntityResponseDTO>> response = collaboratorController
                                .createCollaborator(requestDTO);

                assertEquals(HttpStatus.CREATED, response.getStatusCode());
                assertEquals("Colaborador creado satisfactoriamente", response.getBody().getMessage());
                assertEquals(collaborator, response.getBody().getData());
        }

        @Test
        @DisplayName("Test update collaborator")
        public void testUpdateCollaborator() {
                Long id = 1L;
                CollaboratorUpdateRequestDTO requestDTO = new CollaboratorUpdateRequestDTO();
                CollaboratorEntityResponseDTO collaborator = new CollaboratorEntityResponseDTO(
                                id, "Felipe Bolaños", "Developer", "2024-12-7", 1000D, 1L);
                when(collaboratorService.updateCollaborator(id, requestDTO)).thenReturn(collaborator);

                ResponseEntity<Api_Response<CollaboratorEntityResponseDTO>> response = collaboratorController
                                .updateCollaborator(id, requestDTO);

                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals("Colaborador actualizado satisfactoriamente", response.getBody().getMessage());
                assertEquals(collaborator, response.getBody().getData());
        }

        @Test
        @DisplayName("Test delete collaborator")
        public void testDeleteCollaborator() {
                Long id = 1L;
                doNothing().when(collaboratorService).deleteCollaborator(id);

                ResponseEntity<Api_Response<CollaboratorEntityResponseDTO>> response = collaboratorController
                                .deleteCollaborator(id);

                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals("Colaborador eliminado satisfactoriamente", response.getBody().getMessage());
                assertEquals(null, response.getBody().getData());
        }
}
