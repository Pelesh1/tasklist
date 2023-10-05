package com.example.tasklist.controller;

import com.example.tasklist.entity.User;
import com.example.tasklist.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/registration")
    public String getRegistration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String postRegistration(Model model, @Valid @ModelAttribute User user, BindingResult result) {
        // Если нашлись ошибки по валидации - выводим сообщение об ошибке
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "redirect:/registration?error";
        }
        // Если такой пользователь существует - выводим сообщение
        if (userService.isUserExists(user.getUsername())) {
            model.addAttribute("user", user);
            return "redirect:/registration?badCredentials";
        }
        // Если всё ок - добавляем нового пользователя в БД
        userService.addUser(user);
        // Выводим сообщение об успешной регистрации
        return "redirect:/registration?success";
    }
}
