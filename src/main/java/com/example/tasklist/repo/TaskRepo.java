package com.example.tasklist.repo;

import com.example.tasklist.entity.Task;
import com.example.tasklist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
}
