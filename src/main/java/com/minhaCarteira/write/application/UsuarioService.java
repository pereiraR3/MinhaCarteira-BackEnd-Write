package com.minhaCarteira.write.application;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import com.minhaCarteira.write.domain.usuario.Usuario;
import com.minhaCarteira.write.domain.usuario.UsuarioRole;
import com.minhaCarteira.write.domain.usuario.DTOs.UsuarioRequestDTO;
import com.minhaCarteira.write.domain.usuario.DTOs.UsuarioResponseDTO;
import com.minhaCarteira.write.domain.usuario.DTOs.UsuarioUpdateDTO;
import com.minhaCarteira.write.domain.usuario.repository.UsuarioRepository;
import com.minhaCarteira.write.infra.security.service.KeycloakUserService;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final KeycloakUserService keycloakUserService;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            KeycloakUserService keycloakUserService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.keycloakUserService = keycloakUserService;
    }

    /**
     * Método para criar um usuário
     * 
     * @param body dados do usuário
     * @return usuário
     */
    @Transactional
    public UsuarioResponseDTO create(UsuarioRequestDTO body) {

        checkIfEmailExists(body.email(), null);

        String passwordEncripty = passwordEncoder.encode(body.senha());

        Usuario usuario = Usuario.builder()
                .nome(body.nome())
                .email(body.email())
                .senha(passwordEncripty)
                .roleList(List.of(UsuarioRole.USER))
                .build();

        keycloakUserService.createUser(
                body.email(), body.email(),
                body.nome(), body.nome(),
                body.senha());

        usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail());

    }

    /**
     * Valida se um e-mail já está em uso por outro usuário.
     * 
     * @param email         O e-mail a ser verificado.
     * @param currentUserId O ID do usuário atual.
     */
    private void checkIfEmailExists(String email, Integer currentUserId) {
        Optional<Usuario> userOptional = usuarioRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            Usuario existingUser = userOptional.get();
            if (currentUserId == null || !existingUser.getId().equals(currentUserId)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado no sistema.");
            }
        }
    }

    /**
     * Atualizar os dados de um determinado usuário
     * 
     * @param updateDTO
     */
    public void update(UsuarioUpdateDTO updateDTO) {
        Usuario usuario = findById(updateDTO.id());

        if (StringUtils.hasText(updateDTO.email()))
            checkIfEmailExists(updateDTO.email(), updateDTO.id());

        Optional.ofNullable(updateDTO.nome()).ifPresent(usuario::setNome);
        Optional.ofNullable(updateDTO.email()).ifPresent(usuario::setEmail);

        usuarioRepository.save(usuario);
    }

    /**
     * Método para buscar um usuário pelo id
     * 
     * @param id id do usuário
     * @return usuário
     */
    public Usuario findById(Integer id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário de ID: " + id + " não encontrado!"));
    }

    /**
     * Método usado para buscar por um usuário pelo seu email
     * 
     * @param email
     * @return
     */
    public UsuarioResponseDTO findByEmail(String email) {
        var usuario = usuarioRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuário de email: " + email + " não encontrado!"));

        return new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }

    /**
     * Delete relacional da entidade User
     * 
     * @param id id do usuário
     */
    @Transactional
    public void deleteById(Integer id) {
        Usuario usuario = findById(id);
        usuarioRepository.delete(usuario);
    }

}
