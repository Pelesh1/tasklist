package com.example.tasklist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(max=255, message = "Длина названия заходит за границы допустимых значений")
    private String title;

    @Length(max=255, message = "Длина описания заходит за границы допустимых значений")
    private String description;

    @Column(name = "deadline_date")
    private Date deadlineDate;

    private Boolean done;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deadlineDate=" + deadlineDate +
                ", done=" + done +
                '}';
    }
}
