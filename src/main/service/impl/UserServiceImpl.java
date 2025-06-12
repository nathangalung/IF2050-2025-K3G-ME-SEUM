package main.service.impl;

import main.model.dto.UserDto;
import main.model.entities.User;
import main.model.enums.UserRole;
import main.model.enums.UserStatus;
import main.repository.interfaces.IUserRepository;
import main.repository.impl.UserRepositoryImpl;
import main.service.interfaces.IUserService;
import main.utils.ValidationUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of IUserService
 */
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;
    
    public UserServiceImpl() {
        this.userRepository = new UserRepositoryImpl();
    }
    
    @Override
    public UserDto authenticate(String email, String password) {
        try {
            System.out.println("🔄 Authenticating user with email: " + email);
            // Use plain text password for now (since database has plain text)
            Optional<User> user = userRepository.findByEmailAndPassword(email, password);
            
            if (user.isPresent()) {
                System.out.println("✅ User authenticated successfully: " + user.get().getUsername());
                return convertToDto(user.get());
            } else {
                System.out.println("❌ Authentication failed for email: " + email);
                // Let's also check if user exists with different password
                Optional<User> userExists = userRepository.findByEmail(email);
                if (userExists.isPresent()) {
                    System.out.println("⚠️  User exists but password doesn't match");
                } else {
                    System.out.println("⚠️  User with email doesn't exist");
                }
                return null;
            }
        } catch (Exception e) {
            System.err.println("❌ Authentication error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public UserDto authenticateByUsername(String username, String password) {
        try {
            Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
            
            if (user.isPresent()) {
                System.out.println("✅ User authenticated successfully: " + user.get().getUsername());
                return convertToDto(user.get());
            } else {
                System.out.println("❌ Authentication failed for username: " + username);
                return null;
            }
        } catch (Exception e) {
            System.err.println("❌ Authentication error: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public UserDto registerUser(String username, String email, String password, String namaLengkap, UserRole role) {
        try {
            System.out.println("🔄 Starting registration for: " + username + " (" + email + ")");
            
            // Validate input
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be empty");
            }
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be empty");
            }
            if (!ValidationUtil.isValidEmail(email)) {
                throw new IllegalArgumentException("Invalid email format");
            }
            validatePassword(password);
            if (namaLengkap == null || namaLengkap.trim().isEmpty()) {
                throw new IllegalArgumentException("Full name cannot be empty");
            }
            
            // Check if email or username already exists
            if (userRepository.existsByEmail(email)) {
                throw new IllegalArgumentException("Email already exists: " + email);
            }
            if (userRepository.existsByUsername(username)) {
                throw new IllegalArgumentException("Username already exists: " + username);
            }
            
            // Create new user with plain text password (for now)
            User user = new User(username.trim(), email.trim(), password, role, namaLengkap.trim());
            System.out.println("🔄 Saving user to database...");
            User savedUser = userRepository.save(user);
            
            System.out.println("✅ User registered successfully: " + savedUser.getUsername() + " with ID: " + savedUser.getUserId());
            return convertToDto(savedUser);
            
        } catch (Exception e) {
            System.err.println("❌ Registration error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }
    
    @Override
    public UserDto createUser(UserDto userDto, String password) {
        try {
            ValidationUtil.validateUserData(userDto);
            validatePassword(password);
            
            // Check if email or username already exists
            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new IllegalArgumentException("Email already exists: " + userDto.getEmail());
            }
            if (userRepository.existsByUsername(userDto.getUsername())) {
                throw new IllegalArgumentException("Username already exists: " + userDto.getUsername());
            }
            
            // Convert DTO to entity and set password
            User user = convertToEntity(userDto);
            user.setPassword(password); // Plain text for now
            
            User savedUser = userRepository.save(user);
            return convertToDto(savedUser);
            
        } catch (Exception e) {
            System.err.println("❌ Create user error: " + e.getMessage());
            throw new RuntimeException("Create user failed: " + e.getMessage(), e);
        }
    }
    
    @Override
    public UserDto findUserById(Long userId) {
        try {
            Optional<User> user = userRepository.findById(userId);
            return user.map(this::convertToDto).orElse(null);
        } catch (Exception e) {
            System.err.println("❌ Find user error: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<UserDto> findAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return users.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("❌ Find all users error: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public boolean isEmailAvailable(String email) {
        try {
            boolean available = !userRepository.existsByEmail(email);
            System.out.println("📊 Email " + email + " available: " + available);
            return available;
        } catch (Exception e) {
            System.err.println("❌ Check email availability error: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean isUsernameAvailable(String username) {
        try {
            boolean available = !userRepository.existsByUsername(username);
            System.out.println("📊 Username " + username + " available: " + available);
            return available;
        } catch (Exception e) {
            System.err.println("❌ Check username availability error: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public void validatePassword(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        if (password.length() > 255) {
            throw new IllegalArgumentException("Password must be less than 255 characters");
        }
    }
    
    @Override
    public void validateUserData(UserDto userDto) {
        ValidationUtil.validateUserData(userDto);
    }
    
    @Override
    public UserDto convertToDto(User user) {
        if (user == null) return null;
        
        return new UserDto(
            user.getUserId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole(),
            user.getNamaLengkap(),
            user.getNoTelepon(),
            user.getTanggalRegistrasi(),
            user.getStatus()
        );
    }
    
    @Override
    public User convertToEntity(UserDto userDto) {
        if (userDto == null) return null;
        
        User user = new User();
        user.setUserId(userDto.getUserId());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());
        user.setNamaLengkap(userDto.getNamaLengkap());
        user.setNoTelepon(userDto.getNoTelepon());
        user.setTanggalRegistrasi(userDto.getTanggalRegistrasi());
        user.setStatus(userDto.getStatus() != null ? userDto.getStatus() : UserStatus.AKTIF);
        
        return user;
    }
    
    // Stub implementations for remaining interface methods
    @Override
    public UserDto updateUser(UserDto userDto) {
        return null;
    }
    
    @Override
    public void deleteUser(Long userId) {
        // Implementation needed
    }
    
    @Override
    public List<UserDto> findUsersByRole(UserRole role) {
        return List.of();
    }
    
    @Override
    public List<UserDto> findUsersByStatus(UserStatus status) {
        return List.of();
    }
    
    @Override
    public List<UserDto> searchUsersByName(String nama) {
        return List.of();
    }
    
    @Override
    public void activateUser(Long userId) {
        // Implementation needed
    }
    
    @Override
    public void deactivateUser(Long userId) {
        // Implementation needed
    }
    
    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        // Implementation needed
    }
    
    @Override
    public long getTotalUsers() {
        return 0;
    }
    
    @Override
    public long getUserCountByRole(UserRole role) {
        return 0;
    }
    
    @Override
    public long getActiveUserCount() {
        return 0;
    }
}