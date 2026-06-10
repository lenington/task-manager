package com.taskmanager.dto;

import java.time.LocalDateTime;
import com.taskmanager.model.enums.TaskPriority;
import com.taskmanager.model.enums.TaskStatus;

/**
 * @author Lenington Rios
 *
 * DTO de saída devolvido pela API ao cliente.
 *
 * <p>
 * Ele contém somente os dados que fazem sentido para a resposta HTTP.
 * </p>
 */
public class TaskResponseDTO {

	private Long id;
	private String title;
	private String description;
	private TaskStatus status;
	private TaskPriority priority;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public TaskPriority getPriority() {
		return priority;
	}

	public void setPriority(TaskPriority priority) {
		this.priority = priority;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
