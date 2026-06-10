package com.taskmanager.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.taskmanager.model.enums.TaskPriority;
import com.taskmanager.model.enums.TaskStatus;

/**
 * @author Lenington Rios
 *
 * DTO de entrada usado para criar ou atualizar tarefas.
 *
 * <p>
 * DTO significa Data Transfer Object. Esta classe representa o contrato do JSON
 * recebido pela API e evita expor a entidade JPA diretamente pro cliente.
 * </p>
 */
public class TaskRequestDTO {

	@NotBlank(message = "Título é obrigatório")
	@Size(max = 100, message = "Título deve ter no máximo 100 caracteres")
	private String title;

	@Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
	private String description;

	@NotNull(message = "Status é obrigatório")
	private TaskStatus status;

	@NotNull(message = "Prioridade é obrigatória")
	private TaskPriority priority;

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
}
