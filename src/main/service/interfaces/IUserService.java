package main.service.interfaces;

import main.model.dto.UserDto;
import main.model.entities.User;
import main.model.enums.UserRole;
import main.model.enums.UserStatus;
import java.util.List;

/**
 * Service interface for User business logic
 */
public interface IUserService {
    
    // User management
    UserDto createUser(UserDto userDto, String password);
    UserDto updateUser(UserDto userDto);
    void deleteUser(Long userId);
    UserDto findUserById(Long userId);
    List<UserDto> findAllUsers();
    
    // Authentication
    UserDto authenticate(String email, String password);
    UserDto authenticateByUsername(String username, String password);
    
    // Registration
    UserDto registerUser(String username, String email, String password, String namaLengkap, UserRole role);
    
    // User queries
    List<UserDto> findUsersByRole(UserRole role);
    List<UserDto> findUsersByStatus(UserStatus status);
    List<UserDto> searchUsersByName(String nama);
    
    // Account management
    void activateUser(Long userId);
    void deactivateUser(Long userId);
    void changePassword(Long userId, String oldPassword, String newPassword);
    
    // Validation
    boolean isEmailAvailable(String email);
    boolean isUsernameAvailable(String username);
    void validateUserData(UserDto userDto);
    void validatePassword(String password);
    
    // Statistics
    long getTotalUsers();
    long getUserCountByRole(UserRole role);
    long getActiveUserCount();
    
    // Utility methods
    UserDto convertToDto(User user);
    User convertToEntity(UserDto userDto);
}