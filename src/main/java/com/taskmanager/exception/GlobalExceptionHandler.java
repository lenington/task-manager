package com.taskmanager.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * @author Lenington Rios
 *
 * Tratador centralizado de exceções da API.
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Converte erro de entidade não encontrada em HTTP 404.
	 *
	 * @param ex exceção lançada pelo service
	 * @return resposta padronizada para recurso inexistente
	 */
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleEntityNotFound(
			EntityNotFoundException ex) {

		ErrorResponse response = new ErrorResponse();
		response.setTimestamp(LocalDateTime.now());
		response.setStatus(HttpStatus.NOT_FOUND.value());
		response.setError("Not Found");
		response.setMessage(ex.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(response);
	}

	/**
	 * Converte falhas de validação de campos em HTTP 400.
	 *
	 * <p>
	 * O retorno é um mapa simples no formato {@code campo -> mensagem}, o que
	 * facilita exibir erros perto dos campos no frontend.
	 * </p>
	 *
	 * @param ex exceção gerada
	 * @return mapa com mensagens por campo inválido
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidation(
			MethodArgumentNotValidException ex) {

		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult()
				.getAllErrors()
				.forEach(error -> {
					String field = ((FieldError) error).getField();
					String message = error.getDefaultMessage();

					errors.put(field, message);
				});

		return ResponseEntity.badRequest().body(errors);
	}

	/**
	 * Trata parâmetros e payloads malformados.
	 *
	 * <p>
	 * Exemplos: status inexistente em query string ou JSON com enum inválido.
	 * Nesses casos o cliente enviou dados incorretos, então a resposta adequada é
	 * HTTP 400.
	 * </p>
	 *
	 * @param ex exceção de conversão/leitura da requisição
	 * @return resposta padronizada de requisição inválida
	 */
	@ExceptionHandler({ MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class })
	public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {
		ErrorResponse response = new ErrorResponse();
		response.setTimestamp(LocalDateTime.now());
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setError("Bad Request");
		response.setMessage("Parâmetro ou payload inválido");

		return ResponseEntity.badRequest().body(response);
	}

	/**
	 * Última barreira para erros inesperados.
	 *
	 *
	 * @param ex exceção não tratada pelos handlers anteriores
	 * @return resposta HTTP 500
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneric(
			Exception ex) {

		ErrorResponse response = new ErrorResponse();
		response.setTimestamp(LocalDateTime.now());
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.setError("Internal Server Error");
		response.setMessage(ex.getMessage());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(response);
	}
}
