package com.igrowker.miniproject.User.Repository;

import com.igrowker.miniproject.User.Model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilePhotoRepository extends JpaRepository<UserEntity, Long> {
}
