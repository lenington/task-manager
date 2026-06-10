package com.taskmanager.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import com.taskmanager.model.enums.TaskPriority;
import com.taskmanager.model.enums.TaskStatus;

/**
 * @author Lenington Rios
 *
 * Entity que representa uma tarefa persistida na tabela {@code tasks}.
 *
 * <p>
 * A entidade é o modelo que o Hibernate usa para mapear objetos Java para linhas do banco de dados.
 * As anotações de coluna definem restrições alinhadas com a migration SQL.
 * </p>
 */
@Entity
@Table(name = "tasks")
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String title;

	@Column(length = 500)
	private String description;

	/**
	 * Status atual da tarefa.
	 *
	 * <p>
	 * {@link EnumType#STRING} salva o nome do enum no banco, por exemplo
	 * {@code TODO}. Isso é mais legível e mais seguro que salvar a posição ordinal.
	 * Porém, em sistemas legados dos quais eu trabalho atualmente, costuma-se utilizar enums com posição ordinal.
	 * </p>
	 */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private TaskStatus status;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private TaskPriority priority;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt; //data de criação

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt; //data de alteração

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

	/**
	 * Callback executado pelo JPA antes de inserir a entidade no banco.
	 *
	 * <p>
	 * Aqui são preenchidas as datas de auditoria e valores padrão caso a tarefa seja criada sem status ou prioridade...
	 * </p>
	 */
	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();

		if (this.status == null)
			this.status = TaskStatus.TODO;

		if (this.priority == null)
			this.priority = TaskPriority.MEDIUM;
	}

	/**
	 * Callback executado pelo JPA antes de atualizar a entity no banco.
	 */
	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}
