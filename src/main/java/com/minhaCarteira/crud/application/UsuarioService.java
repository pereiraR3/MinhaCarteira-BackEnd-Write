package com.minhaCarteira.crud.application;

import com.minhaCarteira.crud.domain.usuario.DTOs.UsuarioByFilterDTO;
import com.minhaCarteira.crud.domain.usuario.DTOs.UsuarioRequestDTO;
import com.minhaCarteira.crud.domain.usuario.DTOs.UsuarioResponseDTO;
import com.minhaCarteira.crud.domain.usuario.DTOs.UsuarioUpdateDTO;
import com.minhaCarteira.crud.domain.usuario.Usuario;
import com.minhaCarteira.crud.domain.usuario.UsuarioRole;
import com.minhaCarteira.crud.domain.usuario.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {


    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Método para criar um usuário
     * @param body dados do usuário
     * @return usuário
     */
    @Transactional
    public UsuarioResponseDTO create(UsuarioRequestDTO body) {

        String passwordEncripty = passwordEncoder.encode(body.senha());

        Usuario usuario = Usuario.builder()
                .nome(body.nome())
                .email(body.email())
                .senha(passwordEncripty)
                .roleList(List.of(UsuarioRole.ROLE_VISITANTE))
                .build();

        usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail());

    }

    /**
     * Método para listar todos os usuários
     * @return lista de usuários
     */
    public Page<UsuarioResponseDTO> findByFilter(Pageable query, UsuarioByFilterDTO filter) {

        Pageable pageable = PageRequest.of(query.getPageNumber(), query.getPageSize());

        Specification<Usuario> spec = construirSpecification(filter.nome(), filter.email(), filter.id());

        Page<Usuario> usuariosPage = usuarioRepository.findAll(spec, pageable);

        return usuariosPage
                .map(usuario ->
                        new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail()));
    }

    /**
     * Função que centraliza a lógica de construção da query dinâmica.
     * @param nome, email e id representados como filtros a serem aplicados.
     * @return A Specification<Usuario> combinada.
     */
    private Specification<Usuario> construirSpecification(String nome, String email, Integer id) {
        List<Specification<Usuario>> specifications = new ArrayList<>();

        if (StringUtils.hasText(nome))
            specifications.add((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));

        if (StringUtils.hasText(email))
            specifications.add((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%"));

        if (id != null)
            specifications.add((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("id"), id));

        return Specification.allOf(specifications);
    }

    /**
     * Atualizar os dados de um determinado usuário
     * @param updateDTO
     */
    public void update(UsuarioUpdateDTO updateDTO) {
        Usuario usuario = findById(updateDTO.id());

        Optional.ofNullable(updateDTO.nome()).ifPresent(usuario::setNome);
        Optional.ofNullable(updateDTO.email()).ifPresent(usuario::setEmail);

        usuarioRepository.save(usuario);
    }

    /**
     * Método para buscar um usuário pelo id
     * @param id id do usuário
     * @return usuário
     */
    public Usuario findById(Integer id) {
        return usuarioRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User de ID: " + id + " não encontrado!"));
    }

    /**
     * Delete relacional da entidade User
     * @param id id do usuário
     */
    @Transactional
    public void deleteById(Integer id) {
        Usuario usuario = findById(id);
        usuarioRepository.delete(usuario);
    }

}
