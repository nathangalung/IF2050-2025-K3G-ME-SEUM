package main.model.dto;

import main.model.enums.UserRole;
import main.model.enums.UserStatus;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for User
 */
public class UserDto {
    private Long userId;
    private String username;
    private String email;
    private UserRole role;
    private String namaLengkap;
    private String noTelepon;
    private LocalDateTime tanggalRegistrasi;
    private UserStatus status;
    
    // Default constructor
    public UserDto() {}
    
    // Constructor for registration
    public UserDto(String username, String email, UserRole role, String namaLengkap) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.namaLengkap = namaLengkap;
    }
    
    // Constructor with all fields
    public UserDto(Long userId, String username, String email, UserRole role, 
                   String namaLengkap, String noTelepon, LocalDateTime tanggalRegistrasi, UserStatus status) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
        this.namaLengkap = namaLengkap;
        this.noTelepon = noTelepon;
        this.tanggalRegistrasi = tanggalRegistrasi;
        this.status = status;
    }
    
    // Getters and setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    
    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }
    
    public String getNoTelepon() { return noTelepon; }
    public void setNoTelepon(String noTelepon) { this.noTelepon = noTelepon; }
    
    public LocalDateTime getTanggalRegistrasi() { return tanggalRegistrasi; }
    public void setTanggalRegistrasi(LocalDateTime tanggalRegistrasi) { this.tanggalRegistrasi = tanggalRegistrasi; }
    
    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
    
    @Override
    public String toString() {
        return "UserDto{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", namaLengkap='" + namaLengkap + '\'' +
                ", status=" + status +
                '}';
    }
}