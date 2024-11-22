package com.igrowker.miniproject.Collaborator.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.igrowker.miniproject.Collaborator.Model.Collaborator;
import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Repository.UserRepository;

@DataJpaTest
public class CollaboratorRepositoryTest {

    private UserEntity userEntity;
    private Collaborator collaborator;

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        collaboratorRepository.deleteAll();
        userRepository.deleteAll();
        
        userEntity = UserEntity.builder()
                .userName("Kevin")
                .lastName("Orozco")
                .email("kevin@gmail.com")
                .password("123456")
                .phoneNumber("1234567890")
                .build();
        userEntity = userRepository.save(userEntity);

        collaborator = Collaborator.builder()
                .name("Felipe Bolaños")
                .service("Developer")
                .amount(1000D)
                .user(userEntity)
                .build();
        collaborator = collaboratorRepository.save(collaborator);
    }

    @Test
    @DisplayName("Test get all collaborator")
    public void shouldGetAllCollaborator() {
        // When
        List<Collaborator> collaboratorList = collaboratorRepository.findAll();

        // Then
        assertEquals(1, collaboratorList.size());
        Collaborator collaborator = collaboratorList.get(0);

        assertEquals(this.collaborator.getId(), collaborator.getId());
        assertEquals("Felipe Bolaños", collaborator.getName());
        assertEquals("Developer", collaborator.getService());
        assertEquals(1000D, collaborator.getAmount());
        assertEquals(userEntity.getId(), collaborator.getUser().getId());
    }

    @Test
    @DisplayName("Test find collaborator by name")
    public void shouldGetCollaboratorByName() {
        // When
        Optional<Collaborator> collaborator = collaboratorRepository.findByName("Felipe Bolaños");

        // Then
        assertEquals(this.collaborator.getId(), collaborator.get().getId());
        assertEquals("Felipe Bolaños", collaborator.get().getName());
        assertEquals("Developer", collaborator.get().getService());
        assertEquals(1000D, collaborator.get().getAmount());
        assertEquals(userEntity.getId(), collaborator.get().getUser().getId());
    }
}
