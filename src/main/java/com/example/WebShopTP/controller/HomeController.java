package com.example.WebShopTP.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/redirect-role")
    public String redirectByRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();


        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login.html";
        }

        String role = auth.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", "").toLowerCase())
                .orElse("");

        return switch (role) {
            case "admin" -> "redirect:/admin.html";
            case "dispatcher" -> "redirect:/dispatcher.html";
            case "storekeeper" -> "redirect:/storekeeper.html";
            case "courier" -> "redirect:/courier.html";
            default -> "redirect:/login.html";
        };
    }

    @GetMapping("/")
    public String index() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/redirect-role";
        }
        return "redirect:/login.html";
    }
}