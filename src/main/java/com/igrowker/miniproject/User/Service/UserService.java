package com.igrowker.miniproject.User.Service;

import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Repository.ProfilePhotoRepository;
import com.igrowker.miniproject.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ProfilePhotoRepository profilePhotoRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getUserProfile(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public UserEntity saveProfilePhoto(MultipartFile photo) throws IOException {
        UserEntity profilePhoto = new UserEntity();
        profilePhoto.setFileName(photo.getOriginalFilename());
        profilePhoto.setFileType(photo.getContentType());
        profilePhoto.setData(photo.getBytes()); // Convert file content to byte array

        return profilePhotoRepository.save(profilePhoto); // Save to database
    }
}
