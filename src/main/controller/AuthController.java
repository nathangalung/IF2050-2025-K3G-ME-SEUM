package main.controller;

import main.model.dto.UserDto;
import main.model.enums.UserRole;
import main.service.interfaces.IUserService;
import main.utils.SessionManager; // FIXED: Correct package
import main.service.impl.UserServiceImpl;

/**
 * Controller for authentication operations
 */
public class AuthController {
    private final IUserService userService;
    private UserDto currentUser; // Store logged-in user
    
    public AuthController() {
        this.userService = new UserServiceImpl();
    }
    
    // In AuthController.java - update login method
    public UserDto login(String email, String password) {
        try {
            UserDto user = userService.authenticate(email, password);
            if (user != null) {
                this.currentUser = user;
                // Also update global session
                SessionManager.getInstance().login(user); // FIXED: Correct reference
                System.out.println("✅ Login successful for: " + user.getUsername());
            }
            return user;
        } catch (Exception e) {
            System.err.println("❌ Login failed: " + e.getMessage());
            return null;
        }
    }

    public void logout() {
    if (currentUser != null) {
        System.out.println("✅ User logged out: " + currentUser.getUsername());
        this.currentUser = null;
        // Also update global session
        SessionManager.getInstance().logout();
        
        // Note: Navigation is handled by UI components
        System.out.println("🔄 Session cleared - UI should redirect to login");
    }
}
    
    public UserDto loginByUsername(String username, String password) {
        try {
            UserDto user = userService.authenticateByUsername(username, password);
            if (user != null) {
                this.currentUser = user;
                System.out.println("✅ Login successful for: " + user.getUsername());
            }
            return user;
        } catch (Exception e) {
            System.err.println("❌ Login failed: " + e.getMessage());
            return null;
        }
    }
    
    public UserDto register(String username, String email, String password, String namaLengkap, UserRole role) {
        try {
            UserDto user = userService.registerUser(username, email, password, namaLengkap, role);
            if (user != null) {
                System.out.println("✅ Registration successful for: " + user.getUsername());
                // Optionally auto-login after registration
                this.currentUser = user;
            }
            return user;
        } catch (Exception e) {
            System.err.println("❌ Registration failed: " + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    public UserDto getCurrentUser() {
        return currentUser;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public boolean isEmailAvailable(String email) {
        return userService.isEmailAvailable(email);
    }
    
    public boolean isUsernameAvailable(String username) {
        return userService.isUsernameAvailable(username);
    }
}