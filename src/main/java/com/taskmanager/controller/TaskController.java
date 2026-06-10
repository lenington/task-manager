package com.taskmanager.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.taskmanager.dto.TaskRequestDTO;
import com.taskmanager.dto.TaskResponseDTO;
import com.taskmanager.model.enums.TaskPriority;
import com.taskmanager.model.enums.TaskStatus;
import com.taskmanager.service.TaskService;

/**
 * @author Lenington Rios
 *
 * Camada HTTP da funcionalidade de tarefas.
 *
 * <p>
 * Esta classe expõe os endpoints REST em {@code /api/tasks}.
 * Ela não contém as regras de negócio...
 * Recebe parametros da requisição, aciona o service e devolve a resposta adequada para o cliente.
 * </p>
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

	private final TaskService service;

	/**
	 * Injeta o service responsável pelas regras da funcionalidade.
	 *
	 * @param service tarefas gerenciado pelo spring boot
	 */
	public TaskController(TaskService service) {
		this.service = service;
	}

	/**
	 * Lista tarefas com filtros opcionais por status e prioridade.
	 *
	 * @param status
	 * @param priority
	 * @return lista de tarefas convertidas para DTO de resposta
	 */
	@GetMapping
	public List<TaskResponseDTO> findAll(@RequestParam(required = false) TaskStatus status, @RequestParam(required = false) TaskPriority priority) {
		return service.findAll(status, priority);
	}

	/**
	 * Busca uma tarefa específica pelo seu id.
	 *
	 * @param id identificador da tarefa
	 * @return tarefa
	 */
	@GetMapping("/{id}")
	public TaskResponseDTO findById(@PathVariable Long id) {
		return service.findById(id);
	}

	/**
	 * Cria uma nova tarefa.
	 *
	 * <p>
	 * A anotação {@link Valid} aciona as validações declaradas no
	 * {@link TaskRequestDTO}, como título obrigatório e tamanho máximo dos campos.
	 * </p>
	 *
	 * @param dto dados enviados pelo cliente
	 * @return tarefa criada
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TaskResponseDTO create(@Valid @RequestBody TaskRequestDTO dto) {
		return service.create(dto);
	}

	/**
	 * Atualiza todos os campos editáveis de uma tarefa existente.
	 *
	 * @param id identificador da tarefa
	 * @param dto novos dados da tarefa
	 * @return tarefa atualizada
	 */
	@PutMapping("/{id}")
	public TaskResponseDTO update(@PathVariable Long id, @Valid @RequestBody TaskRequestDTO dto) {
		return service.update(id, dto);
	}

	/**
	 * Remove uma tarefa atraves do id.
	 *
	 * @param id
	 */
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}
}
