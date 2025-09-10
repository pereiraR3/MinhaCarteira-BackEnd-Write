package com.minhaCarteira.write.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.minhaCarteira.write.application.UsuarioService;
import com.minhaCarteira.write.domain.usuario.DTOs.UsuarioRequestDTO;
import com.minhaCarteira.write.domain.usuario.DTOs.UsuarioResponseDTO;
import com.minhaCarteira.write.domain.usuario.DTOs.UsuarioUpdateDTO;

@RestController
@RequestMapping("api/usuario")
public class UsuarioController {

        private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

        private final UsuarioService usuarioService;

        public UsuarioController(UsuarioService usuarioService) {
                this.usuarioService = usuarioService;
        }

        @Operation(summary = "Criação de um novo usuário.", description = "Cria um novo usuário com base nas informações fornecidas.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso."),
                        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos."),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
        })
        @PostMapping("/create")
        public ResponseEntity<UsuarioResponseDTO> create(@RequestBody @Valid UsuarioRequestDTO body,
                        UriComponentsBuilder uriComponentsBuilder) {
                logger.info("Requisição para criação de usuário recebida: {}", body.email());
                UsuarioResponseDTO userResponseDTO = usuarioService.create(body);
                logger.info("Usuário criado com sucesso: ID = {}, Email = {}", userResponseDTO.id(),
                                userResponseDTO.email());
                var uri = uriComponentsBuilder.path("/usuario/{id}").buildAndExpand(userResponseDTO.id()).toUri();
                return ResponseEntity.created(uri).body(userResponseDTO);
        }

        @Operation(summary = "Deleção relacional de um usuário.", description = "Deleta um usuário com base no ID fornecido.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso."),
                        @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
        })
        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<?> deleteById(@PathVariable Integer id) {
                logger.info("Requisição para deletar usuário com ID: {}", id);
                usuarioService.deleteById(id);
                logger.info("Usuário com ID {} deletado com sucesso.", id);
                return ResponseEntity.noContent().build();
        }

        @Operation(summary = "Atualizar todos os dados de um usuário", description = "Atualiza todos os dados de um usuário com base nas informações fornecidas no corpo da requisição.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Dados atualizados com sucesso."),
                        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos."),
                        @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
        })
        @PutMapping(path = "/update")
        @PreAuthorize("hasAnyRole('ADMIN', 'VISITANTE') or authentication.name == #dto.email()")
        public ResponseEntity<?> update(@RequestBody @Valid UsuarioUpdateDTO dto) {
                logger.info("Requisição para atualizar usuário: {}", dto.email());
                usuarioService.update(dto);
                logger.info("Usuário com ID {} atualizado com sucesso.", dto.id());
                return ResponseEntity.noContent().build();
        }

}
