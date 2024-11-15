package com.igrowker.miniproject.User.Service;

import com.igrowker.miniproject.User.Dto.UserProfileResponseDTO;
import com.igrowker.miniproject.User.Dto.UserUpdateRequestDTO;
import com.igrowker.miniproject.User.Exception.UserNotFoundException;
import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Value("${upload.path}")
    private String uploadPath;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    public UserProfileResponseDTO getUserProfile(Long id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return new UserProfileResponseDTO(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "Usuario no encontrado"
            );
        }
        return new UserProfileResponseDTO(
                user.getId(),
                user.getUserName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getCountry(),
                "Usuario encontrado satisfactoriamente"
        );
    }

    public UserProfileResponseDTO updateUserProfile(Long id, UserUpdateRequestDTO userEntity) {
        UserEntity user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("Usuario no encontrado");
        }

        if (!user.getUserName().equals(userEntity.getUserName())) {
            user.setUserName(userEntity.getUserName());
        }
        if (!user.getLastName().equals(userEntity.getLastName())) {
            user.setLastName(userEntity.getLastName());
        }
        if (!user.getEmail().equals(userEntity.getEmail())) {
            user.setEmail(userEntity.getEmail());
        }
        if (!user.getPhoneNumber().equals(userEntity.getPhoneNumber())) {
            user.setPhoneNumber(userEntity.getPhoneNumber());
        }
        if (!user.getCountry().equals(userEntity.getCountry())) {
            user.setCountry(userEntity.getCountry());
        }

        userRepository.save(user);

        return new UserProfileResponseDTO(
                user.getId(),
                user.getUserName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getCountry(),
                "Usuario actualizado satisfactoriamente"
        );
    }

    public String saveProfilePhoto(MultipartFile photo) throws IOException {
        if (photo.isEmpty()) {
            throw new IOException("Empty file");
        }

        // Generar un nombre de archivo único
        String uniqueFileName = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
        Path filePath = Paths.get(uploadPath, uniqueFileName);

        // Asegúrese de que el directorio exista
        Files.createDirectories(filePath.getParent());

        // Guarde el archivo localmente
        Files.copy(photo.getInputStream(), filePath);

        return filePath.toString();
    }
}
