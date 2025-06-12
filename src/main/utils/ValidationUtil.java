package main.utils;

import main.model.dto.ArtefakDto;
import main.model.dto.UserDto;

// Utility untuk validasi data
public class ValidationUtil {
    
    public static boolean validateArtefakData(ArtefakDto artefakDto) {
        if (artefakDto == null) {
            throw new IllegalArgumentException("ArtefakDto cannot be null");
        }
        
        if (artefakDto.getNamaArtefak() == null || artefakDto.getNamaArtefak().trim().isEmpty()) {
            throw new IllegalArgumentException("Nama artefak cannot be empty");
        }
        
        if (artefakDto.getDeskripsiArtefak() == null || artefakDto.getDeskripsiArtefak().trim().isEmpty()) {
            throw new IllegalArgumentException("Deskripsi artefak cannot be empty");
        }
        
        if (artefakDto.getAsalDaerah() == null || artefakDto.getAsalDaerah().trim().isEmpty()) {
            throw new IllegalArgumentException("Asal daerah cannot be empty");
        }
        
        if (artefakDto.getPeriode() == null || artefakDto.getPeriode().trim().isEmpty()) {
            throw new IllegalArgumentException("Periode cannot be empty");
        }
        
        return true;
    }
    
    // Add User validation methods
    public static boolean validateUserData(UserDto userDto) {
        if (userDto == null) {
            throw new IllegalArgumentException("UserDto cannot be null");
        }
        
        if (userDto.getUsername() == null || userDto.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        
        if (userDto.getEmail() == null || userDto.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        
        if (!isValidEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        if (userDto.getNamaLengkap() == null || userDto.getNamaLengkap().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        
        return true;
    }
    
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    public static boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return phone.matches("^[0-9+\\-\\s()]+$");
    }
    
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    public static boolean isValidLength(String value, int minLength, int maxLength) {
        if (value == null) {
            return false;
        }
        int length = value.trim().length();
        return length >= minLength && length <= maxLength;
    }
    
    public static boolean validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number");
        }
        return true;
    }
    
    public static boolean validateString(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
        return true;
    }
}