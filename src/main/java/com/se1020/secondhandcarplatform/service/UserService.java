package com.se1020.secondhandcarplatform.service;

import com.se1020.secondhandcarplatform.model.User;
import com.se1020.secondhandcarplatform.util.FileHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final String USERS_FILE = "data/users.txt";
    private List<User> users;

    public UserService() {
        loadUsers();
    }

    private void loadUsers() {
        users = new ArrayList<>();
        List<String> lines = FileHandler.readFromFile(USERS_FILE);

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 6) {
                User user = new User();
                user.setId(parts[0]);
                user.setUsername(parts[1]);
                user.setPassword(parts[2]);
                user.setEmail(parts[3]);
                user.setPhone(parts[4]);
                user.setRole(parts[5]);
                users.add(user);
            }
        }

        // Create admin user if no users exist
        if (users.isEmpty()) {
            User admin = new User("admin", "admin123", "admin@example.com", "1234567890", "ADMIN");
            users.add(admin);
            saveUsers();
        }
    }

    private void saveUsers() {
        FileHandler.saveToFile(users, USERS_FILE);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public Optional<User> getUserById(String id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public Optional<User> getUserByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    public User saveUser(User user) {
        // Remove existing user if it's an update
        users.removeIf(u -> u.getId().equals(user.getId()));
        users.add(user);
        saveUsers();
        return user;
    }

    public boolean deleteUser(String id) {
        boolean removed = users.removeIf(user -> user.getId().equals(id));
        if (removed) {
            saveUsers();
        }
        return removed;
    }

    public Optional<User> authenticate(String username, String password) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst();
    }
}
