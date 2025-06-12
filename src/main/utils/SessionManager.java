package main.utils;

import main.model.dto.UserDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Central session management for ME-SEUM application
 * Handles user login/logout state across the application
 */
public class SessionManager {
    private static SessionManager instance;
    private UserDto currentUser;
    private LocalDateTime loginTime;
    private List<SessionListener> listeners;
    
    private SessionManager() {
        this.listeners = new ArrayList<>();
    }
    
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    /**
     * Login user and notify all listeners
     */
    public void login(UserDto user) {
        this.currentUser = user;
        this.loginTime = LocalDateTime.now();
        System.out.println("🔐 Session started for: " + user.getUsername() + " (" + user.getRole() + ")");
        notifySessionChanged();
    }
    
    /**
     * Logout current user and notify all listeners
     */
    public void logout() {
        if (currentUser != null) {
            System.out.println("🔓 Session ended for: " + currentUser.getUsername());
            this.currentUser = null;
            this.loginTime = null;
            notifySessionChanged();
        }
    }
    
    /**
     * Check if user is logged in
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Get current logged-in user
     */
    public UserDto getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Get current user role
     */
    public String getCurrentRole() {
        return currentUser != null ? currentUser.getRole().name() : "GUEST";
    }
    
    /**
     * Get login time
     */
    public LocalDateTime getLoginTime() {
        return loginTime;
    }
    
    /**
     * Add session listener
     */
    public void addSessionListener(SessionListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Remove session listener
     */
    public void removeSessionListener(SessionListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Notify all listeners of session change
     */
    private void notifySessionChanged() {
        for (SessionListener listener : listeners) {
            listener.onSessionChanged(currentUser);
        }
    }
    
    /**
     * Interface for session change notifications
     */
    public interface SessionListener {
        void onSessionChanged(UserDto user);
    }
}