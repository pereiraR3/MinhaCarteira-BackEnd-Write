package com.minhaCarteira.crud.api;

import com.minhaCarteira.crud.application.GastoService;
import com.minhaCarteira.crud.domain.gasto.DTOs.GastoByFilterDTO;
import com.minhaCarteira.crud.domain.gasto.DTOs.GastoRequestDTO;
import com.minhaCarteira.crud.domain.gasto.DTOs.GastoResponseDTO;
import com.minhaCarteira.crud.domain.gasto.DTOs.GastoUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/gasto")
public class GastoController {

	private final GastoService gastoService;

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
	public ResponseEntity<GastoResponseDTO> create(@RequestBody @Valid GastoRequestDTO body, UriComponentsBuilder uriComponentsBuilder) {
		GastoResponseDTO response = gastoService.create(body);
		var uri = uriComponentsBuilder.path("/gasto/{id}").buildAndExpand(response.id()).toUri();
		return ResponseEntity.created(uri).body(response);
	}

	@Operation(summary = "Listagem de um único gasto.", description = "Lista um único gasto com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Gasto encontrado com sucesso."),
			@ApiResponse(responseCode = "404", description = "Gasto não encontrado."),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
	})
	@GetMapping("/{id}")
	public ResponseEntity<GastoResponseDTO> findById(@PathVariable Integer id) {
		var gasto = gastoService.findById(id);
		GastoResponseDTO dto = new GastoResponseDTO(
				gasto.getId(),
				gasto.getValor(),
				gasto.getDescricao(),
				gasto.getNome(),
				gasto.getData(),
				gasto.getDataCriacao(),
				gasto.getCategoria() != null ? gasto.getCategoria().getId() : null,
				gasto.getUsuario() != null ? gasto.getUsuario().getId() : null
		);
		return ResponseEntity.ok().body(dto);
	}

	@Operation(summary = "Listagem paginada de gastos.", description = "Lista gastos com paginação e filtros opcionais.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Gastos listados com sucesso."),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
	})
	@GetMapping("/find-by-filter")
	public ResponseEntity<Page<GastoResponseDTO>> findByFilter(
			@ParameterObject @PageableDefault(sort = "data", direction = Sort.Direction.DESC) Pageable pageable,
			@ParameterObject GastoByFilterDTO gastoByFilterDTO
	) {
		Page<GastoResponseDTO> page = gastoService.findByFilter(pageable, gastoByFilterDTO);
		return ResponseEntity.ok().body(page);
	}

	@Operation(summary = "Deleção de um gasto.", description = "Deleta um gasto com base no ID fornecido.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Gasto deletado com sucesso."),
			@ApiResponse(responseCode = "404", description = "Gasto não encontrado."),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Integer id) {
		gastoService.deleteById(id);
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
		gastoService.update(dto);
		return ResponseEntity.noContent().build();
	}

}

