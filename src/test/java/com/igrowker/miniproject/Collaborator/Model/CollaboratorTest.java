package com.igrowker.miniproject.Collaborator.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.igrowker.miniproject.User.Model.UserEntity;

public class CollaboratorTest {

    UserEntity user = new UserEntity();

    @BeforeEach
    public void setUp() {
        user.setId(1L);
        user.setUserName("John");
        user.setLastName("Doe");
        user.setEmail("john@gmail.com");
        user.setPhoneNumber("1234567890");
    }

    @Test
    @DisplayName("Test collaborator creation - assertAll")
    public void shouldCreateCollaborator() {
        // Given
        Collaborator collaborator = Collaborator.builder()
                .id(1L)
                .name("John Doe")
                .service("CEO")
                .amount(500)
                .user(user)
                .build();

        // Then
        assertNotNull(collaborator, "Collaborator should not be null");
        assertEquals(1L, collaborator.getId());
        assertEquals("John Doe", collaborator.getName());
        assertEquals("CEO", collaborator.getService());
        assertEquals(500, collaborator.getAmount());
        assertEquals(1L, user.getId());
    }
}