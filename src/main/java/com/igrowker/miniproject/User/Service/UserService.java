package com.igrowker.miniproject.User.Service;
import com.igrowker.miniproject.TaxObligation.Service.TaxService;
import com.igrowker.miniproject.User.Dto.UserProfileResponseDTO;
import com.igrowker.miniproject.User.Dto.UserUpdateRequestDTO;
import com.igrowker.miniproject.User.Exception.InvalidUserIdException;
import com.igrowker.miniproject.User.Exception.UserNotFoundException;
import com.igrowker.miniproject.User.Model.RegisterVerification2FA;
import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Repository.RegisterVerification2FARepository;
import com.igrowker.miniproject.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RegisterVerification2FARepository registerVerification2FARepository;

    private final TaxService taxService;

    @Value("${upload.path}")
    private String uploadPath;


    public UserService(UserRepository userRepository, RegisterVerification2FARepository registerVerification2FARepository, TaxService taxService) {
        this.userRepository = userRepository;
        this.registerVerification2FARepository = registerVerification2FARepository;
        this.taxService = taxService;
    }

    public void saveUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    public UserProfileResponseDTO getUserProfile(Long id) {
        if (id <= 0) {
            throw new InvalidUserIdException("El id del usuario debe ser mayor a 0");
        }

        return userRepository.findById(id)
                .map(user -> new UserProfileResponseDTO(
                        user.getId(),
                        user.getUserName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getCountry()
                ))
                .orElseThrow(() -> new UserNotFoundException("Usuario con id " + id + " no encontrado"));
    }

    public UserProfileResponseDTO updateUserProfile(Long id, UserUpdateRequestDTO userEntity) {
        if (id <= 0) {
            throw new InvalidUserIdException("El id del usuario debe ser mayor a 0");
        }
        UserEntity user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("Usuario con id " + id + " no encontrado");
        }

        user.setUserName(userEntity.getUserName());
        user.setLastName(userEntity.getLastName());
        user.setEmail(userEntity.getEmail());
        user.setPhoneNumber(userEntity.getPhoneNumber());
        user.setCountry(userEntity.getCountry());

        userRepository.save(user);

        return new UserProfileResponseDTO(
                user.getId(),
                user.getUserName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getCountry()
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

    public void deleteUser(Long id){
        if (id <= 0) {
            throw new InvalidUserIdException("El id del usuario debe ser mayor a 0");
        }
        UserEntity user = userRepository.findById(id).orElse(null);
        RegisterVerification2FA fa = registerVerification2FARepository.findByEmail(user != null ? user.getEmail() : "null").orElseThrow(() -> new UserNotFoundException("No se encontro el registro de 2FA para este usuario"));
        if (user == null) {
            throw new UserNotFoundException("Usuario con id " + id + " no encontrado");
        }
        userRepository.delete(user);
        registerVerification2FARepository.delete(fa);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUserName(username).orElse(null);
    }
}
