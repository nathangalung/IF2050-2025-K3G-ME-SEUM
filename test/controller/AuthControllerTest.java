package test.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import main.controller.AuthController;
import main.model.dto.UserDto;
import main.model.enums.UserRole;
import main.service.interfaces.IUserService;
import main.utils.SessionManager;

/**
 * Unit tests for AuthController class
 * Tests authentication, registration, and user management functionality
 */
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private IUserService mockUserService;
    
    @Mock
    private SessionManager mockSessionManager;
    
    private AuthController authController;
    private UserDto testUser;

    @BeforeEach
    void setUp() {
        // Create test user
        testUser = new UserDto();
        testUser.setUserId(1L);  // Changed from setId to setUserId
        testUser.setUsername("testuser");
        testUser.setEmail("test@email.com");
        testUser.setNamaLengkap("Test User");
        testUser.setRole(UserRole.CUSTOMER);
        
        // Create AuthController with mocked dependencies
        authController = new AuthController() {
            // Override to inject mock
            {
                try {
                    var field = AuthController.class.getDeclaredField("userService");
                    field.setAccessible(true);
                    field.set(this, mockUserService);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @Test
    void testLoginSuccess() {
        // Arrange
        String email = "test@email.com";
        String password = "password123";
        when(mockUserService.authenticate(email, password)).thenReturn(testUser);
        
        try (MockedStatic<SessionManager> mockedStatic = mockStatic(SessionManager.class)) {
            mockedStatic.when(SessionManager::getInstance).thenReturn(mockSessionManager);
            
            // Act
            UserDto result = authController.login(email, password);
            
            // Assert
            assertNotNull(result);
            assertEquals(testUser.getUsername(), result.getUsername());
            assertEquals(testUser.getEmail(), result.getEmail());
            assertTrue(authController.isLoggedIn());
            assertEquals(testUser, authController.getCurrentUser());
            
            // Verify interactions
            verify(mockUserService).authenticate(email, password);
            verify(mockSessionManager).login(testUser);
        }
    }

    @Test
    void testLoginFailure() {
        // Arrange
        String email = "invalid@email.com";
        String password = "wrongpassword";
        when(mockUserService.authenticate(email, password)).thenReturn(null);
        
        // Act
        UserDto result = authController.login(email, password);
        
        // Assert
        assertNull(result);
        assertFalse(authController.isLoggedIn());
        assertNull(authController.getCurrentUser());
        
        // Verify interactions
        verify(mockUserService).authenticate(email, password);
    }

    @Test
    void testLoginException() {
        // Arrange
        String email = "test@email.com";
        String password = "password123";
        when(mockUserService.authenticate(email, password))
            .thenThrow(new RuntimeException("Database connection failed"));
        
        // Act
        UserDto result = authController.login(email, password);
        
        // Assert
        assertNull(result);
        assertFalse(authController.isLoggedIn());
        
        // Verify interactions
        verify(mockUserService).authenticate(email, password);
    }

    @Test
    void testLoginByUsernameSuccess() {
        // Arrange
        String username = "testuser";
        String password = "password123";
        when(mockUserService.authenticateByUsername(username, password)).thenReturn(testUser);
        
        // Act
        UserDto result = authController.loginByUsername(username, password);
        
        // Assert
        assertNotNull(result);
        assertEquals(testUser.getUsername(), result.getUsername());
        assertTrue(authController.isLoggedIn());
        
        // Verify interactions
        verify(mockUserService).authenticateByUsername(username, password);
    }

    @Test
    void testLoginByUsernameFailure() {
        // Arrange
        String username = "invaliduser";
        String password = "wrongpassword";
        when(mockUserService.authenticateByUsername(username, password)).thenReturn(null);
        
        // Act
        UserDto result = authController.loginByUsername(username, password);
        
        // Assert
        assertNull(result);
        assertFalse(authController.isLoggedIn());
    }

    @Test
    void testLogoutWithLoggedInUser() {
        // Arrange - First login a user
        when(mockUserService.authenticate("test@email.com", "password123")).thenReturn(testUser);
        
        try (MockedStatic<SessionManager> mockedStatic = mockStatic(SessionManager.class)) {
            mockedStatic.when(SessionManager::getInstance).thenReturn(mockSessionManager);
            
            authController.login("test@email.com", "password123");
            assertTrue(authController.isLoggedIn());
            
            // Act
            authController.logout();
            
            // Assert
            assertFalse(authController.isLoggedIn());
            assertNull(authController.getCurrentUser());
            
            // Verify interactions
            verify(mockSessionManager).logout();
        }
    }

    @Test
    void testLogoutWithoutLoggedInUser() {
        // Arrange - No user logged in
        assertFalse(authController.isLoggedIn());
        
        try (MockedStatic<SessionManager> mockedStatic = mockStatic(SessionManager.class)) {
            mockedStatic.when(SessionManager::getInstance).thenReturn(mockSessionManager);
            
            // Act
            authController.logout();
            
            // Assert
            assertFalse(authController.isLoggedIn());
            assertNull(authController.getCurrentUser());
            
            // Verify no interactions with SessionManager
            verify(mockSessionManager, never()).logout();
        }
    }

    @Test
    void testRegisterSuccess() {
        // Arrange
        String username = "newuser";
        String email = "newuser@email.com";
        String password = "password123";
        String namaLengkap = "New User";
        UserRole role = UserRole.CUSTOMER;
        
        UserDto newUser = new UserDto();
        newUser.setUserId(2L);  // Changed from setId to setUserId
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setNamaLengkap(namaLengkap);
        newUser.setRole(role);
        
        when(mockUserService.registerUser(username, email, password, namaLengkap, role))
            .thenReturn(newUser);
        
        // Act
        UserDto result = authController.register(username, email, password, namaLengkap, role);
        
        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(email, result.getEmail());
        assertEquals(namaLengkap, result.getNamaLengkap());
        assertEquals(role, result.getRole());
        assertTrue(authController.isLoggedIn()); // Auto-login after registration
        assertEquals(newUser, authController.getCurrentUser());
        
        // Verify interactions
        verify(mockUserService).registerUser(username, email, password, namaLengkap, role);
    }

    @Test
    void testRegisterFailure() {
        // Arrange
        String username = "existinguser";
        String email = "existing@email.com";
        String password = "password123";
        String namaLengkap = "Existing User";
        UserRole role = UserRole.CUSTOMER;
        
        when(mockUserService.registerUser(username, email, password, namaLengkap, role))
            .thenThrow(new RuntimeException("Email already exists"));
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authController.register(username, email, password, namaLengkap, role);
        });
        
        assertEquals("Email already exists", exception.getMessage());
        assertFalse(authController.isLoggedIn());
        
        // Verify interactions
        verify(mockUserService).registerUser(username, email, password, namaLengkap, role);
    }

    @Test
    void testIsEmailAvailable() {
        // Arrange
        String availableEmail = "available@email.com";
        String unavailableEmail = "taken@email.com";
        
        when(mockUserService.isEmailAvailable(availableEmail)).thenReturn(true);
        when(mockUserService.isEmailAvailable(unavailableEmail)).thenReturn(false);
        
        // Act & Assert
        assertTrue(authController.isEmailAvailable(availableEmail));
        assertFalse(authController.isEmailAvailable(unavailableEmail));
        
        // Verify interactions
        verify(mockUserService).isEmailAvailable(availableEmail);
        verify(mockUserService).isEmailAvailable(unavailableEmail);
    }

    @Test
    void testIsUsernameAvailable() {
        // Arrange
        String availableUsername = "availableuser";
        String unavailableUsername = "takenuser";
        
        when(mockUserService.isUsernameAvailable(availableUsername)).thenReturn(true);
        when(mockUserService.isUsernameAvailable(unavailableUsername)).thenReturn(false);
        
        // Act & Assert
        assertTrue(authController.isUsernameAvailable(availableUsername));
        assertFalse(authController.isUsernameAvailable(unavailableUsername));
        
        // Verify interactions
        verify(mockUserService).isUsernameAvailable(availableUsername);
        verify(mockUserService).isUsernameAvailable(unavailableUsername);
    }

    @Test
    void testGetCurrentUserWhenLoggedIn() {
        // Arrange
        when(mockUserService.authenticate("test@email.com", "password123")).thenReturn(testUser);
        
        try (MockedStatic<SessionManager> mockedStatic = mockStatic(SessionManager.class)) {
            mockedStatic.when(SessionManager::getInstance).thenReturn(mockSessionManager);
            
            authController.login("test@email.com", "password123");
            
            // Act
            UserDto currentUser = authController.getCurrentUser();
            
            // Assert
            assertNotNull(currentUser);
            assertEquals(testUser, currentUser);
        }
    }

    @Test
    void testGetCurrentUserWhenNotLoggedIn() {
        // Act
        UserDto currentUser = authController.getCurrentUser();
        
        // Assert
        assertNull(currentUser);
    }

    @Test
    void testIsLoggedInWhenLoggedIn() {
        // Arrange
        when(mockUserService.authenticate("test@email.com", "password123")).thenReturn(testUser);
        
        try (MockedStatic<SessionManager> mockedStatic = mockStatic(SessionManager.class)) {
            mockedStatic.when(SessionManager::getInstance).thenReturn(mockSessionManager);
            
            authController.login("test@email.com", "password123");
            
            // Act & Assert
            assertTrue(authController.isLoggedIn());
        }
    }

    @Test
    void testIsLoggedInWhenNotLoggedIn() {
        // Act & Assert
        assertFalse(authController.isLoggedIn());
    }

    @Test
    void testMultipleLoginsOverwritePreviousUser() {
        // Arrange
        UserDto firstUser = new UserDto();
        firstUser.setUserId(1L);  // Changed from setId to setUserId
        firstUser.setUsername("user1");
        firstUser.setEmail("user1@email.com");
        
        UserDto secondUser = new UserDto();
        secondUser.setUserId(2L);  // Changed from setId to setUserId
        secondUser.setUsername("user2");
        secondUser.setEmail("user2@email.com");
        
        when(mockUserService.authenticate("user1@email.com", "pass1")).thenReturn(firstUser);
        when(mockUserService.authenticate("user2@email.com", "pass2")).thenReturn(secondUser);
        
        try (MockedStatic<SessionManager> mockedStatic = mockStatic(SessionManager.class)) {
            mockedStatic.when(SessionManager::getInstance).thenReturn(mockSessionManager);
            
            // Act
            authController.login("user1@email.com", "pass1");
            assertEquals(firstUser, authController.getCurrentUser());
            
            authController.login("user2@email.com", "pass2");
            
            // Assert
            assertEquals(secondUser, authController.getCurrentUser());
            assertNotEquals(firstUser, authController.getCurrentUser());
        }
    }
}