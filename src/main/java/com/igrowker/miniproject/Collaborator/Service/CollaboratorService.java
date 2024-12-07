package com.igrowker.miniproject.Collaborator.Service;

import com.igrowker.miniproject.Collaborator.DTO.CollaboratorCreateRequestDTO;
import com.igrowker.miniproject.Collaborator.DTO.CollaboratorEntityResponseDTO;
import com.igrowker.miniproject.Collaborator.DTO.CollaboratorUpdateRequestDTO;
import com.igrowker.miniproject.Collaborator.Exception.BadCollaboratorBodyRequestException;
import com.igrowker.miniproject.Collaborator.Exception.CollaboratorNotFoundException;
import com.igrowker.miniproject.Collaborator.Exception.InvalidCollaboratorFieldException;
import com.igrowker.miniproject.Collaborator.Exception.InvalidCollaboratorIdException;
import com.igrowker.miniproject.Collaborator.Model.Collaborator;
import com.igrowker.miniproject.Collaborator.Repository.CollaboratorRepository;
import com.igrowker.miniproject.User.Exception.UserNotFoundException;
import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;
    private final UserRepository userRepository;

    public CollaboratorService(CollaboratorRepository collaboratorRepository, UserRepository userRepository) {
        this.collaboratorRepository = collaboratorRepository;
        this.userRepository = userRepository;
    }

    public List<CollaboratorEntityResponseDTO> getCollaborators(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario con id " + id + " no encontrado"));

        List<Collaborator> collaborators = collaboratorRepository.findByUserId(id);

        return collaborators.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    public CollaboratorEntityResponseDTO getCollaborator(Long id) {
        validateCollaboratorId(id);

        return collaboratorRepository.findById(id)
                .map(collaborator -> new CollaboratorEntityResponseDTO(
                        collaborator.getId(),
                        collaborator.getName(),
                        collaborator.getService(),
                        collaborator.getDate(),
                        collaborator.getAmount(),
                        collaborator.getUser().getId()))
                .orElseThrow(() -> new CollaboratorNotFoundException("Colaborador con id " + id + " no encontrado"));
    }

    public CollaboratorEntityResponseDTO createCollaborator(CollaboratorCreateRequestDTO collaboratorCreateRequestDTO) {
        validateCollaboratorCreateFields(collaboratorCreateRequestDTO);

        UserEntity user = userRepository.findById(collaboratorCreateRequestDTO.getUser_id())
                .orElseThrow(() -> new UserNotFoundException(
                        "Usuario con id " + collaboratorCreateRequestDTO.getUser_id() + " no encontrado"));

        Collaborator collaborator = Collaborator.builder()
                .name(collaboratorCreateRequestDTO.getName())
                .service(collaboratorCreateRequestDTO.getService())
                .date(collaboratorCreateRequestDTO.getDate())
                .amount(collaboratorCreateRequestDTO.getAmount())
                .user(user)
                .build();

        collaboratorRepository.save(collaborator);

        return new CollaboratorEntityResponseDTO(
                collaborator.getId(),
                collaborator.getName(),
                collaborator.getService(),
                collaborator.getDate(),
                collaborator.getAmount(),
                collaborator.getUser().getId());
    }

    public CollaboratorEntityResponseDTO updateCollaborator(Long id,
            CollaboratorUpdateRequestDTO collaboratorUpdateRequestDTO) {
        validateCollaboratorId(id);

        Collaborator collaborator = collaboratorRepository.findById(id)
                .orElseThrow(() -> new CollaboratorNotFoundException("Colaborador con id " + id + " no encontrado"));

        validateCollaboratorUpdateFields(collaboratorUpdateRequestDTO);

        collaborator.setName(collaboratorUpdateRequestDTO.getName());
        collaborator.setService(collaboratorUpdateRequestDTO.getService());
        collaborator.setAmount(collaboratorUpdateRequestDTO.getAmount());

        collaboratorRepository.save(collaborator);

        return new CollaboratorEntityResponseDTO(
                collaborator.getId(),
                collaborator.getName(),
                collaborator.getService(),
                collaborator.getDate(),
                collaborator.getAmount(),
                collaborator.getUser().getId());
    }

    public void deleteCollaborator(Long id) {
        validateCollaboratorId(id);

        Collaborator collaborator = collaboratorRepository.findById(id)
                .orElseThrow(() -> new CollaboratorNotFoundException("Colaborador con id " + id + " no encontrado"));

        collaboratorRepository.delete(collaborator);
    }

    public CollaboratorEntityResponseDTO entityToDTO(Collaborator collaborator) {
        return new CollaboratorEntityResponseDTO(
                collaborator.getId(),
                collaborator.getName(),
                collaborator.getService(),
                collaborator.getDate(),
                collaborator.getAmount(),
                collaborator.getUser().getId());
    }

    public void validateCollaboratorCreateFields(CollaboratorCreateRequestDTO collaboratorCreateRequestDTO) {
        if (collaboratorCreateRequestDTO == null) {
            throw new BadCollaboratorBodyRequestException("El objeto de solicitud del colaborador no puede ser nulo");
        }

        validateCollaboratorFields(collaboratorCreateRequestDTO.getName(),
                collaboratorCreateRequestDTO.getService(),
                collaboratorCreateRequestDTO.getDate(), collaboratorCreateRequestDTO.getAmount());
    }

    public void validateCollaboratorUpdateFields(CollaboratorUpdateRequestDTO collaboratorUpdateRequestDTO) {
        if (collaboratorUpdateRequestDTO == null) {
            throw new BadCollaboratorBodyRequestException("El objeto de solicitud del colaborador no puede ser nulo");
        }

        validateCollaboratorFields(collaboratorUpdateRequestDTO.getName(), collaboratorUpdateRequestDTO.getService(),
                collaboratorUpdateRequestDTO.getDate(),
                collaboratorUpdateRequestDTO.getAmount());
    }

    private void validateCollaboratorFields(String name, String service, String date, Double amount) {
        if (amount <= 0) {
            throw new InvalidCollaboratorFieldException("El monto del colaborador debe ser mayor a 0");
        }

        if (date == null || date.isEmpty()) {
            throw new InvalidCollaboratorFieldException("La fecha del colaborador no puede estar vacía");
        }

        if (name == null || name.isEmpty()) {
            throw new InvalidCollaboratorFieldException("El nombre del colaborador no puede estar vacío");
        }

        if (service == null || service.isEmpty()) {
            throw new InvalidCollaboratorFieldException("El servicio del colaborador no puede estar vacío");
        }
    }

    public void validateCollaboratorId(Long id) {
        if (id <= 0) {
            throw new InvalidCollaboratorIdException("El id del colaborador debe ser mayor a 0");
        }
    }
}
