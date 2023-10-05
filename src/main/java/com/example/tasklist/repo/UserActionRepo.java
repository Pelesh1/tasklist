package com.example.tasklist.repo;

import com.example.tasklist.entity.UserAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActionRepo extends JpaRepository<UserAction, Long> {
}
