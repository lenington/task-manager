package com.taskmanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.taskmanager.dto.TaskRequestDTO;
import com.taskmanager.dto.TaskResponseDTO;
import com.taskmanager.model.Task;
import com.taskmanager.model.enums.TaskPriority;
import com.taskmanager.model.enums.TaskStatus;
import com.taskmanager.repository.TaskRepository;

/**
 * @author Lenington Rios
 *
 * Camada Service da funcionalidade de tarefas.
 *
 * <p>
 * O Service concentra as regras de negocio e o acesso ao repositorio.
 * O controller deve continuar fino, apenas recebendo requisições HTTP e delegando o trabalho para esta classe.
 * </p>
 */
@Service
public class TaskService {

	private static final Logger log = LoggerFactory.getLogger(TaskService.class);

	private final TaskRepository taskRepository;

	/**
	 * Recebe o repositorio por injeção de dependência.
	 *
	 * @param taskRepository repositorio JPA de tarefas
	 */
	public TaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	/**
	 * Lista tarefas aplicando os filtros opcionais via API.
	 *
	 * <p>
	 * A escolha do método do repositório depende de quais filtros foram preenchidos.
	 * Sem filtros, todas as tarefas são listadas.
	 * </p>
	 *
	 * @param status
	 * @param priority
	 * @return lista de tarefas em formato de resposta da API
	 */
	public List<TaskResponseDTO> findAll(TaskStatus status, TaskPriority priority) {
		log.debug("Buscando tarefas com status={} e priority={}", status, priority);

		List<Task> tasks = new ArrayList<Task>();

		if (status != null && priority != null) {
			tasks = taskRepository.findByStatusAndPriority(status, priority);
		} else if (status != null) {
			tasks = taskRepository.findByStatus(status);
		} else if (priority != null) {
			tasks = taskRepository.findByPriority(priority);
		} else {
			tasks = taskRepository.findAll();
		}

		log.debug("{} tarefa(s) encontrada(s)", tasks.size());

		List<TaskResponseDTO> response = new ArrayList<TaskResponseDTO>();

		for (Task task : tasks)
			response.add(toResponse(task));

		return response;
	}

	/**
	 * Busca uma tarefa pelo id.
	 *
	 * @param id
	 * @return tarefa DTO
	 * @throws EntityNotFoundException quando o id não existe
	 */
	public TaskResponseDTO findById(Long id) {
		log.debug("Buscando tarefa id={}", id);

		Optional<Task> optionalTask = taskRepository.findById(id);
		if (!optionalTask.isPresent()) {
			log.warn("Tarefa não encontrada id={}", id);
			throw new EntityNotFoundException("Tarefa não encontrada com id: " + id);
		}

		/*Apenas uma versão mais robusta utilizando lambda:

		Task task = taskRepository.findById(id)
				.orElseThrow(() -> {
					log.warn("Tarefa não encontrada id={}", id);
					return new EntityNotFoundException("Tarefa não encontrada com id: " + id);
				});
		*/

		Task task = optionalTask.get();

		return toResponse(task);
	}

	/**
	 * Cria uma tarefa a partir dos dados recebidos via API.
	 *
	 * <p>
	 * O DTO de entrada é convertido para entidade antes de salvar.
	 * Após a salvar, a entity é convertida para DTO como resposta.
	 * </p>
	 *
	 * @param dto dados validados da tarefa
	 * @return tarefa criada
	 */
	public TaskResponseDTO create(TaskRequestDTO dto) {
		log.info("Criando nova tarefa: title={}, priority={}", dto.getTitle(), dto.getPriority());
		Task task = new Task();
		task.setTitle(dto.getTitle());
		task.setDescription(dto.getDescription());
		task.setStatus(dto.getStatus());
		task.setPriority(dto.getPriority());

		Task saved = taskRepository.save(task);
		log.info("Tarefa criada com sucesso id={}", saved.getId());
		return toResponse(saved);
	}

	/**
	 * Atualiza uma tarefa existente.
	 *
	 * <p>
	 * Primeiro a tarefa é buscada no banco. Se não existir, uma exceção é lançada
	 * e o handler global transforma isso em resposta HTTP 404.
	 * </p>
	 *
	 * @param id identificador da tarefa
	 * @param dto dados alterados
	 * @return tarefa atualizada
	 */
	public TaskResponseDTO update(Long id, TaskRequestDTO dto) {
		log.info("Atualizando tarefa id={}", id);
		Task task = taskRepository.findById(id)
				.orElseThrow(() -> {
					log.warn("Tarefa não encontrada para atualização id={}", id);
					return new EntityNotFoundException("Tarefa não encontrada com id: " + id);
				});

		task.setTitle(dto.getTitle());
		task.setDescription(dto.getDescription());
		task.setStatus(dto.getStatus());
		task.setPriority(dto.getPriority());

		Task updated = taskRepository.save(task);
		log.info("Tarefa atualizada com sucesso id={}", updated.getId());
		return toResponse(updated);
	}

	/**
	 * Remove uma tarefa existente.
	 *
	 * @param id
	 * @throws EntityNotFoundException quando o id não existe
	 */
	public void delete(Long id) {
		log.info("Removendo tarefa id={}", id);
		if (!taskRepository.existsById(id)) {
			log.warn("Tarefa não encontrada para remoção id={}", id);
			throw new EntityNotFoundException("Tarefa não encontrada com id: " + id);
		}
		taskRepository.deleteById(id);
		log.info("Tarefa removida com sucesso id={}", id);
	}

	/**
	 * Converte a entidade JPA para o DTO retornado pela API.
	 *
	 * @param task entidade persistida
	 * @return DTO de resposta
	 */
	private TaskResponseDTO toResponse(Task task) {
		TaskResponseDTO response = new TaskResponseDTO();

		response.setId(task.getId());
		response.setTitle(task.getTitle());
		response.setDescription(task.getDescription());
		response.setStatus(task.getStatus());
		response.setPriority(task.getPriority());
		response.setCreatedAt(task.getCreatedAt());
		response.setUpdatedAt(task.getUpdatedAt());

		return response;
	}
}
