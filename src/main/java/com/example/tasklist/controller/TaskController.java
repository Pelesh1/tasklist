package com.example.tasklist.controller;

import com.example.tasklist.entity.Task;
import com.example.tasklist.entity.User;
import com.example.tasklist.repo.TaskRepo;
import com.example.tasklist.service.UserActionService;
import com.example.tasklist.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class TaskController {
    private TaskRepo taskRepo;
    private UserService userService;
    private UserActionService userActionService;

    @GetMapping("/create")
    public String getCreate() {
        return "create";
    }

    @PostMapping("/create")
    public String postCreate(Model model,
                             @ModelAttribute Task task,
                             @AuthenticationPrincipal User user,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/create?error";
        }
        task.setUser((User) userService.loadUserByUsername(user.getUsername()));
        task.setDone(false);
        Task savedTask = taskRepo.save(task);
        userActionService.addAction(savedTask, user);
        return "redirect:/create?success";
    }

    @GetMapping("/list")
    public String getList(Model model, @AuthenticationPrincipal User user) {
        List<Task> tasks = taskRepo.findByUser(user);
        model.addAttribute("tasks", tasks);
        return "list";
    }

    @GetMapping("/edit")
    public String getEditTask(Model model,
                              @AuthenticationPrincipal User user,
                              @RequestParam Long id) {
        Task task = taskRepo.findById(id).orElse(null);
        model.addAttribute("task", task);
        return "edit";
    }

    @PostMapping("/edit")
    public String postEditTask(@Valid @ModelAttribute Task task,
                               @AuthenticationPrincipal User user,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/edit?error";
        }
        task.setUser(user);
        Task savedTask = taskRepo.save(task);
        userActionService.editAction(savedTask, user);
        return "redirect:/list";
    }

    @PostMapping("/toggle")
    public String postToggle(@RequestParam Long id, @AuthenticationPrincipal User user) {
        // Получаем задание по id
        Task task = taskRepo.findById(id).orElse(null);
        if (task != null) {
            // Меняем галочку у задания
            task.setDone(task.getDone() == null ? false : !task.getDone());
            // Меняем значение из базы
            Task savedTask = taskRepo.save(task);
            if (savedTask.getDone()) {
                // Добавляем в логи событие о выполнении задания
                userActionService.checkAction(savedTask, user);
            } else {
                // Добавляем в логи событие об отмене выполнении задания
                userActionService.uncheckAction(savedTask, user);
            }
        }
        return "redirect:/list";
    }

    @GetMapping("/delete")
    public String getDeleteTask(@RequestParam Long id, @AuthenticationPrincipal User user) {
        // Добавляем в логи событие об удалении задания
        Task taskToDelete = taskRepo.findById(id).orElse(null);
        userActionService.deleteAction(taskToDelete, user);
        // Удаляем значение из базы
        taskRepo.deleteById(id);
        return "redirect:/list";
    }
}
