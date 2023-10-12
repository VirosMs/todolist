package com.charles.todolist.task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ID
 * Usuario (ID_Usuario)
 * Descriçao
 * Titulo
 * Data de Inicio
 * Data de Fim
 * Prioridade
 * Data de Criaçao
 */

@Data
@Entity(name = "tb_tasks")
public class TaskModel {


    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String description;

    @Column(length = 50)
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    @Column(length = 50)
    private String priority;
    private UUID userId;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
