package com.example.WebShopTP.controller;

import com.example.WebShopTP.entities.User;
import com.example.WebShopTP.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody User u) {
        try {
            User created = userService.registerUser(u.getLogin(), u.getPassword(), u.getRole());
            return ResponseEntity.ok(created);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User u) {
        return userService.authenticate(u.getLogin(), u.getPassword())
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(401).body("Bad credentials"));
    }

    @GetMapping("/current")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body(null);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("login", auth.getName());
        response.put("role", auth.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse(""));

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (!userService.deleteUser(id))
            return ResponseEntity.badRequest().body("Not found");
        return ResponseEntity.ok("Deleted");
    }

    @PutMapping("/role/{id}")
    public ResponseEntity<String> changeRole(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String newRole = request.get("role");
        if (!userService.changeRole(id, newRole))
            return ResponseEntity.badRequest().body("Not found");
        return ResponseEntity.ok("Role updated");
    }
}
