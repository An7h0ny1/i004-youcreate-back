package com.igrowker.miniproject.User.Model;

public class UserTest {
    // Test cases for User model

    /** 
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
    }*/
}
