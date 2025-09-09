package com.minhaCarteira.crud.application;

import com.minhaCarteira.crud.domain.categoria.Categoria;
import com.minhaCarteira.crud.domain.categoria.DTOs.CategoriaByFilterDTO;
import com.minhaCarteira.crud.domain.categoria.DTOs.CategoriaResponseDTO;
import com.minhaCarteira.crud.domain.categoria.repository.CategoriaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Método  utilizado para filtrar dados de categoria
     * @param query
     * @param filter
     * @return
     */
    public Page<CategoriaResponseDTO> findByFilter(Pageable query, CategoriaByFilterDTO filter) {
        Pageable pageable = PageRequest.of(query.getPageNumber(), query.getPageSize());

        Specification<Categoria> spec = buildSpecification(filter.id(), filter.nome());

        return categoriaRepository.findAll(spec, pageable).map(this::mapToResponse);
    }

    /**
     * Utilizado para construir uma especificação para o JPA filtrar os dados
     * @param id
     * @param nome
     * @return
     */
    private Specification<Categoria> buildSpecification(Integer id, String nome) {
        List<Specification<Categoria>> specs = new ArrayList<>();

        if (id != null)
            specs.add((root, query, cb) -> cb.equal(root.get("id"), id));

        if (StringUtils.hasText(nome))
            specs.add((root, query, cb) -> cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));

        return Specification.allOf(specs);
    }

    /**
     * Método usado para buscar por uma determinada categoria
     * @param id
     * @return
     */
    public CategoriaResponseDTO findById(Integer id) {
        var categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gasto de ID: " + id + " não encontrado!"));

        return mapToResponse(categoria);
    }

    /**
     * Método usado para mapear uma entidade em DTO
     * @param categoria
     * @return
     */
    private CategoriaResponseDTO mapToResponse(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getCorIdentificacao()
        );
    }
}
