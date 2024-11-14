package com.igrowker.miniproject.User.Service;

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

    private final UserRepository userRepository ;

    @Value("${upload.path}")
    private String uploadPath;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getUserProfile(Long id){
        return userRepository.findById(id).orElse(null);
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
