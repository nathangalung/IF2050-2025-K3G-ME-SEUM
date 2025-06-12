package main.repository.interfaces;

import main.model.entities.User;
import main.model.enums.UserRole;
import main.model.enums.UserStatus;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity
 */
public interface IUserRepository {
    
    // Basic CRUD operations
    User save(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    
    // Authentication methods
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findByUsernameAndPassword(String username, String password);
    
    // Query methods
    List<User> findByRole(UserRole role);
    List<User> findByStatus(UserStatus status);
    List<User> findByRoleAndStatus(UserRole role, UserStatus status);
    List<User> findByNamaLengkapContaining(String nama);
    
    // Validation methods
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    
    // Status management
    void updateStatus(Long userId, UserStatus status);
    
    // Pagination support
    List<User> findAllWithPagination(int offset, int limit);
    long count();
    long countByRole(UserRole role);
    long countByStatus(UserStatus status);
}