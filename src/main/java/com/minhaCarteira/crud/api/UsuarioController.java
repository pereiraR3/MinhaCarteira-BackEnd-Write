package com.minhaCarteira.crud.api;

import com.minhaCarteira.crud.application.UsuarioService;
import com.minhaCarteira.crud.domain.usuario.DTOs.UsuarioByFilterDTO;
import com.minhaCarteira.crud.domain.usuario.DTOs.UsuarioRequestDTO;
import com.minhaCarteira.crud.domain.usuario.DTOs.UsuarioResponseDTO;
import com.minhaCarteira.crud.domain.usuario.DTOs.UsuarioUpdateDTO;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;

    public UsuarioController(UsuarioService usuarioService, AuthenticationManager authenticationManager) {
        this.usuarioService = usuarioService;
        this.authenticationManager = authenticationManager;
    }

    @Operation(
            summary = "Criação de um novo usuário.",
            description = "Cria um novo usuário com base nas informações fornecidas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PostMapping("/create")
    public ResponseEntity<UsuarioResponseDTO> create(@RequestBody @Valid UsuarioRequestDTO body, UriComponentsBuilder uriComponentsBuilder) {
        UsuarioResponseDTO userResponseDTO = usuarioService.create(body);
        var uri = uriComponentsBuilder.path("/usuario/{id}").buildAndExpand(userResponseDTO.id()).toUri();
        return ResponseEntity.created(uri).body(userResponseDTO);
    }

    @Operation(
            summary = "Listagem de um único usuário.",
            description = "Lista um único usuário com base no ID fornecido."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> findById(@PathVariable Integer id) {
        var usuario = usuarioService.findById(id);
        UsuarioResponseDTO userResponseDTO = new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail());
        return ResponseEntity.ok().body(userResponseDTO);
    }

    @Operation(
            summary = "Listagem de um único usuário.",
            description = "Lista um único usuário com base no Email fornecido."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/search-by-email/{email}")
    public ResponseEntity<UsuarioResponseDTO> findByEmail(@PathVariable String email) {
        var usuarioDto = usuarioService.findByEmail(email);
        return ResponseEntity.ok().body(usuarioDto);
    }

    @Operation(
            summary = "Listagem de todos os usuários.",
            description = "Lista todos os usuários registrados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários encontrados com sucesso."),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @GetMapping("/find-by-filter")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UsuarioResponseDTO>> findByFilter(
            @ParameterObject @PageableDefault(sort = "nome", direction = Sort.Direction.ASC) Pageable pageable,
            @ParameterObject UsuarioByFilterDTO usuarioByFilterDTO
            ) {
        Page<UsuarioResponseDTO> userResponseDTOList = usuarioService.findByFilter(pageable, usuarioByFilterDTO);
        return ResponseEntity.ok().body(userResponseDTOList);
    }


    @Operation(
            summary = "Deleção relacional de um usuário.",
            description = "Deleta um usuário com base no ID fornecido."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Atualizar todos os dados de um usuário",
            description = "Atualiza todos os dados de um usuário com base nas informações fornecidas no corpo da requisição."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Dados atualizados com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos."),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    })
    @PutMapping(path = "/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'VISITANTE') or authentication.name == #dto.email()")
    public ResponseEntity<?> update(@RequestBody @Valid UsuarioUpdateDTO dto) {
        usuarioService.update(dto);
        return ResponseEntity.noContent().build();
    }

}
