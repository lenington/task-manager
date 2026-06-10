package com.taskmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.taskmanager.model.Task;
import com.taskmanager.model.enums.TaskPriority;
import com.taskmanager.model.enums.TaskStatus;

/**
 * @author Lenington Rios
 *
 * Repositório de acesso à tabela {@code tasks}.
 *
 * <p>
 * Ao estender {@link JpaRepository}, a aplicação ganha operações prontas como
 * {@code findAll}, {@code findById}, {@code save}, {@code existsById} e
 * {@code deleteById}. Os métodos abaixo usam query methods do Spring Data:
 * o Spring interpreta o nome do método e gera a consulta automaticamente.
 * </p>
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

	/**
	 * Busca tarefas por status.
	 *
	 * @param status
	 * @return tarefas
	 */
	List<Task> findByStatus(TaskStatus status);

	/**
	 * Busca tarefas por prioridade.
	 *
	 * @param priority
	 * @return tarefas
	 */
	List<Task> findByPriority(TaskPriority priority);

	/**
	 * Busca tarefas que atendam aos dois filtros (status e prioridade).
	 *
	 * @param status
	 * @param priority
	 * @return tarefas
	 */
	List<Task> findByStatusAndPriority(TaskStatus status, TaskPriority priority);
}
