package com.igrowker.miniproject.User.Service;

import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Repository.UserRepository;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository ;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getUserProfile(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public String createUser(UserEntity entity) {
        try {
             // Crear el objeto UserEntity y mapear los datos del DTO de solicitud
            UserEntity userEntity = new UserEntity();
            userEntity.setUserName(entity.getEmail());
            userEntity.setPassword(entity.getPassword()); // Encriptar la contraseña
            userEntity.setEmail(entity.getEmail());
            userEntity.setCountry(entity.getCountry());
            userEntity.setRole(entity.getRole());
        
            //
            userRepository.save(userEntity);


            return "creado con exito";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
