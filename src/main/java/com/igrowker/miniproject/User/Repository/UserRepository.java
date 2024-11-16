package com.igrowker.miniproject.User.Repository;

import com.igrowker.miniproject.User.Model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByuserName(String username);
    Optional<UserEntity> findByEmail(String email);
}