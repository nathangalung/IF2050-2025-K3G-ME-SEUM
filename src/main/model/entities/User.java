package main.model.entities;

import main.model.enums.UserRole;
import main.model.enums.UserStatus;
import java.time.LocalDateTime;

/**
 * User entity representing users table in database
 * Supports CURATOR, CUSTOMER, and CLEANER roles
 */
public class User {
    private Long userId;
    private String username;
    private String email;
    private String password;
    private UserRole role;
    private String namaLengkap;
    private String noTelepon;
    private LocalDateTime tanggalRegistrasi;
    private UserStatus status;
    
    // Default constructor
    public User() {
        this.tanggalRegistrasi = LocalDateTime.now();
        this.status = UserStatus.AKTIF;
    }
    
    // Constructor for new user registration
    public User(String username, String email, String password, UserRole role, String namaLengkap) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.namaLengkap = namaLengkap;
    }
    
    // Constructor with all fields
    public User(Long userId, String username, String email, String password, UserRole role, 
                String namaLengkap, String noTelepon, LocalDateTime tanggalRegistrasi, UserStatus status) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.namaLengkap = namaLengkap;
        this.noTelepon = noTelepon;
        this.tanggalRegistrasi = tanggalRegistrasi;
        this.status = status;
    }
    
    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
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
    
    // Business logic methods
    public boolean isActive() {
        return status == UserStatus.AKTIF;
    }
    
    public boolean isCurator() {
        return role == UserRole.CURATOR;
    }
    
    public boolean isCustomer() {
        return role == UserRole.CUSTOMER;
    }
    
    public boolean isCleaner() {
        return role == UserRole.CLEANER;
    }
    
    public void activate() {
        this.status = UserStatus.AKTIF;
    }
    
    public void deactivate() {
        this.status = UserStatus.NONAKTIF;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", namaLengkap='" + namaLengkap + '\'' +
                ", noTelepon='" + noTelepon + '\'' +
                ", tanggalRegistrasi=" + tanggalRegistrasi +
                ", status=" + status +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId != null && userId.equals(user.userId);
    }
    
    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }
}