package com.example.WebShopTP.service;

import com.example.WebShopTP.entities.User;
import com.example.WebShopTP.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    // Регистрация нового пользователя
    public User registerUser(String login, String password, String role) {
        if (userRepo.existsByLogin(login))
            throw new RuntimeException("Логин занят!");

        User user = new User(login, password, role);
        return userRepo.save(user);
    }

    // Проверка логина и пароля при входе
    public Optional<User> authenticate(String login, String password) {
        return userRepo.findByLogin(login)
                .filter(user -> user.getPassword().equals(password));
    }

    // Получить всех пользователей
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    // Удалить пользователя (уволить)
    public boolean deleteUser(Long id) {
        if (!userRepo.existsById(id))
            return false;
        userRepo.deleteById(id);
        return true;
    }

    // Изменить роль пользователя
    public boolean changeRole(Long id, String newRole) {
        Optional<User> userOpt = userRepo.findById(id);
        if (!userOpt.isPresent())
            return false;
        User user = userOpt.get();
        user.setRole(newRole);
        userRepo.save(user);
        return true;
    }

    // Получить пользователя по ID
    public Optional<User> getUserById(Long id) {
        return userRepo.findById(id);
    }

    // Получить пользователя по логину
    public Optional<User> getUserByLogin(String login) {
        return userRepo.findByLogin(login);
    }

    // Получить всех сотрудников (не администраторов)
    public List<User> getAllEmployees() {
        return userRepo.findAll().stream()
                .filter(u -> !u.getRole().equals("ADMIN"))
                .toList();
    }
}
