package com.minhaCarteira.write.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.minhaCarteira.write.application.GastoService;
import com.minhaCarteira.write.domain.gasto.DTOs.GastoRequestDTO;
import com.minhaCarteira.write.domain.gasto.DTOs.GastoResponseDTO;
import com.minhaCarteira.write.domain.gasto.DTOs.GastoUpdateDTO;

@RestController
@RequestMapping("api/gasto")
public class GastoController {

	private final GastoService gastoService;
	private static final Logger logger = LoggerFactory.getLogger(GastoController.class);

	public GastoController(GastoService gastoService) {
		this.gastoService = gastoService;
	}

	@Operation(summary = "Criação de um novo gasto.", description = "Cria um novo gasto com base nas informações fornecidas.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Gasto criado com sucesso."),
			@ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos."),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
	})
	@PostMapping("/create")
	public ResponseEntity<GastoResponseDTO> create(@RequestBody @Valid GastoRequestDTO body,
			UriComponentsBuilder uriComponentsBuilder) {
		logger.info("Recebida requisição para criação de gasto: {}", body);
		GastoResponseDTO response = gastoService.create(body);
		logger.info("Gasto criado com sucesso: ID = {}", response.id());
		var uri = uriComponentsBuilder.path("/gasto/{id}").buildAndExpand(response.id()).toUri();
		return ResponseEntity.created(uri).body(response);
	}

	@Operation(summary = "Deleção de um gasto.", description = "Deleta um gasto com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Gasto deletado com sucesso."),
			@ApiResponse(responseCode = "404", description = "Gasto não encontrado."),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Integer id) {
		logger.info("Recebida requisição para deletar gasto com ID: {}", id);
		gastoService.deleteById(id);
		logger.info("Gasto com ID {} deletado com sucesso.", id);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Atualizar um gasto.", description = "Atualiza os dados de um gasto.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Gasto atualizado com sucesso."),
			@ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos."),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
	})
	@PutMapping(path = "/update")
	public ResponseEntity<?> update(@RequestBody @Valid GastoUpdateDTO dto) {
		logger.info("Recebida requisição para atualização de gasto: {}", dto);
		gastoService.update(dto);
		logger.info("Gasto com ID {} atualizado com sucesso.", dto.id());
		return ResponseEntity.noContent().build();
	}

}
