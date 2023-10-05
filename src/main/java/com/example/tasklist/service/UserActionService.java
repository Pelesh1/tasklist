package com.example.tasklist.service;

import com.example.tasklist.entity.Task;
import com.example.tasklist.entity.User;
import com.example.tasklist.entity.UserAction;
import com.example.tasklist.repo.UserActionRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class UserActionService {
    private UserActionRepo userActionRepo;

    private enum ActionType {
        ADD,
        REMOVE,
        EDIT,
        CHECK,
        UNCHECK,
    }

    public List<UserAction> getAll() {
        return userActionRepo.findAll();
    }

    public void addAction(Task task, User user) {
        saveAction(ActionType.ADD.name(), task, user);
    }

    public void deleteAction(Task task, User user) {
        saveAction(ActionType.REMOVE.name(), task, user);
    }

    public void editAction(Task task, User user) {
        saveAction(ActionType.EDIT.name(), task, user);
    }

    public void checkAction(Task task, User user) {
        saveAction(ActionType.CHECK.name(), task, user);
    }

    public void uncheckAction(Task task, User user) {
        saveAction(ActionType.UNCHECK.name(), task, user);
    }

    private void saveAction(String action, Task task, User user) {
        UserAction userAction = new UserAction();
        userAction.setDescription(action + " " + task.toString());
        userAction.setUser(user);
        userAction.setDateActions(new Date(System.currentTimeMillis()));
        userActionRepo.save(userAction);
    }
}
