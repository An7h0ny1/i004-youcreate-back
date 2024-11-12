package com.igrowker.miniproject.User.Model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserTest {
    // Test cases for User model

    @Test
    @DisplayName("Should create user with valid data")
    public void shouldCreateUser(){
        // Given
        User user = new User("John Doe", "john@gmail.com", "John123.", false, "USA");

        // Then
        assertNotNull(user, "User should not be null");
        assertEquals("John Doe", user.getName());
        assertEquals("john@gmail.com", user.getEmail());
        assertEquals("John123.", user.getPassword());
        assertEquals(false, user.isAdmin());
        assertEquals("USA", user.getCountry());
    }
}
