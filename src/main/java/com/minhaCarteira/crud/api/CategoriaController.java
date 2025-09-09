package com.minhaCarteira.crud.api;

import com.minhaCarteira.crud.application.CategoriaService;
import com.minhaCarteira.crud.domain.categoria.DTOs.CategoriaByFilterDTO;
import com.minhaCarteira.crud.domain.categoria.DTOs.CategoriaResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/categoria")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @Operation(summary = "Listagem paginada de categorias.", description = "Lista categorias com paginação e filtros opcionais.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categorias listadas com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/find-by-filter")
    public ResponseEntity<Page<CategoriaResponseDTO>> findByFilter(
            @ParameterObject @PageableDefault(sort = "data", direction = Sort.Direction.DESC) Pageable pageable,
            @ParameterObject CategoriaByFilterDTO categoriaByFilterDTO
    ) {
        Page<CategoriaResponseDTO> page = categoriaService.findByFilter(pageable, categoriaByFilterDTO);
        return ResponseEntity.ok().body(page);
    }

    @Operation(summary = "Listagem de uma única categoria.", description = "Lista uma única categoria com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria encontrada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> findById(@PathVariable Integer id) {
        var categoriaDto = categoriaService.findById(id);

        return ResponseEntity.ok().body(categoriaDto);
    }

}
