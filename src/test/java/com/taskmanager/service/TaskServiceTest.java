package com.taskmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.taskmanager.dto.TaskRequestDTO;
import com.taskmanager.model.Task;
import com.taskmanager.model.enums.TaskPriority;
import com.taskmanager.model.enums.TaskStatus;
import com.taskmanager.repository.TaskRepository;

/**
 * @author Lenington Rios
 *
 * Testes unitários do service de tarefas.
 *
 * <p>
 * Aqui o repositório usando Mockito.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

	@Mock
	private TaskRepository repository;

	@InjectMocks
	private TaskService service;

	/**
	 * Garante que o service retorne o DTO correto quando o repositório encontra a tarefa solicitada
	 */
	@Test
	void shouldFindTaskById() {
		Task task = new Task();
		task.setId(1L);
		task.setTitle("Revisar incidente");
		task.setStatus(TaskStatus.TODO);
		task.setPriority(TaskPriority.HIGH);

		when(repository.findById(1L)).thenReturn(Optional.of(task));

		assertEquals("Revisar incidente", service.findById(1L).getTitle());
	}

	/**
	 * Verifica se o service monta direitinho a entity e chama o repositório para salvar.
	 */
	@Test
	void shouldCreateTask() {
		TaskRequestDTO dto = new TaskRequestDTO();
		dto.setTitle("Nova tarefa");
		dto.setStatus(TaskStatus.TODO);
		dto.setPriority(TaskPriority.MEDIUM);

		Task task = new Task();
		task.setId(1L);
		task.setTitle(dto.getTitle());
		task.setStatus(dto.getStatus());
		task.setPriority(dto.getPriority());

		when(repository.save(any(Task.class))).thenReturn(task);

		assertNotNull(service.create(dto));
		verify(repository).save(any(Task.class));
	}

	/**
	 * Confirma que uma busca por id inexistente gera a exceção esperada.
	 */
	@Test
	void shouldThrowWhenTaskDoesNotExist() {
		when(repository.findById(99L)).thenReturn(Optional.empty());

		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
				() -> service.findById(99L));

		assertEquals("Tarefa não encontrada com id: 99", exception.getMessage());
	}
}
