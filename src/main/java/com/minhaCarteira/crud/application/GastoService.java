package com.minhaCarteira.crud.application;

import com.minhaCarteira.crud.domain.categoria.Categoria;
import com.minhaCarteira.crud.domain.categoria.repository.CategoriaRepository;
import com.minhaCarteira.crud.domain.gasto.Gasto;
import com.minhaCarteira.crud.domain.gasto.DTOs.GastoByFilterDTO;
import com.minhaCarteira.crud.domain.gasto.DTOs.GastoRequestDTO;
import com.minhaCarteira.crud.domain.gasto.DTOs.GastoResponseDTO;
import com.minhaCarteira.crud.domain.gasto.DTOs.GastoUpdateDTO;
import com.minhaCarteira.crud.domain.gasto.repository.GastoRepository;
import com.minhaCarteira.crud.domain.usuario.Usuario;
import com.minhaCarteira.crud.domain.usuario.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GastoService {

	private final GastoRepository gastoRepository;
	private final UsuarioRepository usuarioRepository;
	private final CategoriaRepository categoriaRepository;

	public GastoService(GastoRepository gastoRepository, UsuarioRepository usuarioRepository, CategoriaRepository categoriaRepository) {
		this.gastoRepository = gastoRepository;
		this.usuarioRepository = usuarioRepository;
		this.categoriaRepository = categoriaRepository;
	}

	@Transactional
	public GastoResponseDTO create(GastoRequestDTO dto) {
		Usuario usuario = usuarioRepository.findById(dto.usuarioId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

		Categoria categoria = categoriaRepository.findById(dto.categoriaId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada."));

	Gasto gasto = Gasto.builder()
		.valor(dto.valor())
		.descricao(dto.descricao())
		.nome(dto.nome())
		.data(dto.data())
		.categoria(categoria)
		.usuario(usuario)
		.build();

		gastoRepository.save(gasto);

		return mapToResponse(gasto);
	}

	public Page<GastoResponseDTO> findByFilter(Pageable query, GastoByFilterDTO filter) {
		Pageable pageable = PageRequest.of(query.getPageNumber(), query.getPageSize());

		Specification<Gasto> spec = buildSpecification(filter.usuarioId(), filter.categoriaId(), filter.nome());

		return gastoRepository.findAll(spec, pageable).map(this::mapToResponse);
	}

	private Specification<Gasto> buildSpecification(Integer usuarioId, Integer categoriaId, String nome) {
		List<Specification<Gasto>> specs = new ArrayList<>();

		if (usuarioId != null) {
			specs.add((root, query, cb) -> cb.equal(root.get("usuario").get("id"), usuarioId));
		}

		if (categoriaId != null) {
			specs.add((root, query, cb) -> cb.equal(root.get("categoria").get("id"), categoriaId));
		}

		if (StringUtils.hasText(nome)) {
			specs.add((root, query, cb) -> cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
		}

		return Specification.allOf(specs);
	}

	public Gasto findById(Integer id) {
		return gastoRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gasto de ID: " + id + " não encontrado!"));
	}

	@Transactional
	public void deleteById(Integer id) {
		Gasto gasto = findById(id);
		gastoRepository.delete(gasto);
	}

	@Transactional
	public void update(GastoUpdateDTO dto) {
		Gasto gasto = findById(dto.id());

	Optional.ofNullable(dto.valor()).ifPresent(gasto::setValor);
	Optional.ofNullable(dto.descricao()).ifPresent(gasto::setDescricao);
	Optional.ofNullable(dto.nome()).ifPresent(gasto::setNome);
	Optional.ofNullable(dto.data()).ifPresent(gasto::setData);

		if (dto.categoriaId() != null) {
			Categoria categoria = categoriaRepository.findById(dto.categoriaId())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada."));
			gasto.setCategoria(categoria);
		}

		gastoRepository.save(gasto);
	}

	private GastoResponseDTO mapToResponse(Gasto gasto) {
		return new GastoResponseDTO(
				gasto.getId(),
				gasto.getValor(),
				gasto.getDescricao(),
				gasto.getNome(),
				gasto.getData(),
				gasto.getDataCriacao(),
				gasto.getCategoria() != null ? gasto.getCategoria().getId() : null,
				gasto.getUsuario() != null ? gasto.getUsuario().getId() : null
		);
	}

}

