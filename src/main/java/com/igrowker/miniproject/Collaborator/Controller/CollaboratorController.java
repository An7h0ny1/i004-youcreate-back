package com.igrowker.miniproject.Collaborator.Controller;

import com.igrowker.miniproject.Collaborator.DTO.CollaboratorCreateRequestDTO;
import com.igrowker.miniproject.Collaborator.DTO.CollaboratorEntityResponseDTO;
import com.igrowker.miniproject.Collaborator.DTO.CollaboratorUpdateRequestDTO;
import com.igrowker.miniproject.Collaborator.Service.CollaboratorService;
import com.igrowker.miniproject.Utils.Api_Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/collaborator")
public class CollaboratorController {

    private final CollaboratorService collaboratorService;

    public CollaboratorController(CollaboratorService collaboratorService) {
        this.collaboratorService = collaboratorService;
    }

    @GetMapping("/all/{userId}")
    @Tag(name = "Collaborator", description = "API for collaborator data.")
    @Operation(summary = "Get Collaborators", description = "Get all collaborators data by user id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Colaboradores encontrados o lista vacía si no hay colaboradores para el usuario"),
            @ApiResponse(responseCode = "400", description = "El id del usuario debe ser mayor a 0 o datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Api_Response<List<CollaboratorEntityResponseDTO>>> getCollaborators(@PathVariable Long userId) {
        List<CollaboratorEntityResponseDTO> response = collaboratorService.getCollaborators(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Api_Response<>(response, "Colaboradores encontrados", 200));
    }

    @GetMapping("/{id}")
    @Tag(name = "Collaborator", description = "API for collaborator data.")
    @Operation(summary = "Get Collaborator", description = "Get collaborator data by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Colaborador encontrado satisfactoriamente"),
            @ApiResponse(responseCode = "400", description = "El id del colaborador debe ser mayor a 0 o datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Colaborador no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Api_Response<CollaboratorEntityResponseDTO>> getCollaborator(@PathVariable Long id) {
        CollaboratorEntityResponseDTO response = collaboratorService.getCollaborator(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Api_Response<>(response, "Colaborador encontrado satisfactoriamente", 200));
    }

    @PostMapping("/create")
    @Tag(name = "Collaborator", description = "API for create collaborator.")
    @Operation(summary = "Create Collaborator", description = "Create a new collaborator.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Colaborador creado satisfactoriamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Api_Response<CollaboratorEntityResponseDTO>> createCollaborator(@RequestBody @Valid CollaboratorCreateRequestDTO collaboratorCreateRequestDTO) {
        CollaboratorEntityResponseDTO response = collaboratorService.createCollaborator(collaboratorCreateRequestDTO);
        System.out.println(response);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Api_Response<>(response, "Colaborador creado satisfactoriamente", 201));
    }

    @PutMapping("/{id}")
    @Tag(name = "Collaborator", description = "API for update collaborator.")
    @Operation(summary = "Update Collaborator", description = "Update a collaborator.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Colaborador actualizado satisfactoriamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Colaborador no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Api_Response<CollaboratorEntityResponseDTO>> updateCollaborator(@PathVariable Long id, @RequestBody @Valid CollaboratorUpdateRequestDTO collaboratorUpdateRequestDTO) {
        CollaboratorEntityResponseDTO response = collaboratorService.updateCollaborator(id, collaboratorUpdateRequestDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Api_Response<>(response, "Colaborador actualizado satisfactoriamente", 200));
    }

    @DeleteMapping("/{id}")
    @Tag(name = "Collaborator", description = "API for delete collaborator.")
    @Operation(summary = "Delete Collaborator", description = "Delete a collaborator.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Colaborador eliminado satisfactoriamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Colaborador no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Api_Response<CollaboratorEntityResponseDTO>> deleteCollaborator(@PathVariable Long id) {
        collaboratorService.deleteCollaborator(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Api_Response<>(null, "Colaborador eliminado satisfactoriamente", 200));
    }
}
