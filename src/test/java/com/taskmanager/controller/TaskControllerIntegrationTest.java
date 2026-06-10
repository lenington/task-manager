package com.taskmanager.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Lenington Rios
 *
 * Testes de integração da API de tarefas.
 *
 * <p>
 * Diferente do teste unitário. O MockMvc simula chamadas HTTP para validar controller, services, repositório,
 * serialização JSON, validações e banco...
 * </p>
 */
@SpringBootTest
@AutoConfigureMockMvc
@Sql(statements = "DELETE FROM tasks", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TaskControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	/**
	 * fluxo principal do CRUD...
	 * além disso, confirmar que a tarefa removida retorna 404.
	 */
	@Test
	void shouldCreateListUpdateAndDeleteTask() throws Exception {
		String payload = "{\"title\":\"Implementar fluxo\",\"description\":\"CRUD ponta a ponta\","
				+ "\"status\":\"TODO\",\"priority\":\"HIGH\"}";

		String created = mockMvc.perform(post("/api/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.title").value("Implementar fluxo"))
				.andExpect(jsonPath("$.priority").value("HIGH"))
				.andReturn()
				.getResponse()
				.getContentAsString();

		String id = created.replaceAll(".*\"id\":(\\d+).*", "$1");

		mockMvc.perform(get("/api/tasks?status=TODO&priority=HIGH"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));

		String updatePayload = "{\"title\":\"Implementar fluxo completo\",\"description\":\"API e tela\","
				+ "\"status\":\"DONE\",\"priority\":\"MEDIUM\"}";

		mockMvc.perform(put("/api/tasks/" + id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(updatePayload))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("Implementar fluxo completo"))
				.andExpect(jsonPath("$.status").value("DONE"));

		mockMvc.perform(delete("/api/tasks/" + id))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/api/tasks/" + id))
				.andExpect(status().isNotFound());
	}

	/**
	 * Garante que campos obrigatórios são bloqueados antes de chegar a 'regra de negócio'.
	 */
	@Test
	void shouldValidateRequiredFields() throws Exception {
		String payload = "{\"title\":\"\",\"description\":\"x\",\"status\":null,\"priority\":null}";

		mockMvc.perform(post("/api/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.title").value("Título é obrigatório"))
				.andExpect(jsonPath("$.status").value("Status é obrigatório"))
				.andExpect(jsonPath("$.priority").value("Prioridade é obrigatória"));
	}

	@Test
	void shouldReturnBadRequestForInvalidFilterEnum() throws Exception {
		mockMvc.perform(get("/api/tasks?status=INVALID"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("Parâmetro inválido"));
	}
}
