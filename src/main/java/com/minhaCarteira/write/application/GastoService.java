package com.minhaCarteira.write.application;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.minhaCarteira.write.domain.categoria.Categoria;
import com.minhaCarteira.write.domain.categoria.repository.CategoriaRepository;
import com.minhaCarteira.write.domain.gasto.Gasto;
import com.minhaCarteira.write.domain.gasto.DTOs.GastoRequestDTO;
import com.minhaCarteira.write.domain.gasto.DTOs.GastoResponseDTO;
import com.minhaCarteira.write.domain.gasto.DTOs.GastoUpdateDTO;
import com.minhaCarteira.write.domain.gasto.repository.GastoRepository;
import com.minhaCarteira.write.domain.usuario.Usuario;
import com.minhaCarteira.write.domain.usuario.repository.UsuarioRepository;

import java.util.Optional;

@Service
public class GastoService {

	private final GastoRepository gastoRepository;
	private final UsuarioRepository usuarioRepository;
	private final CategoriaRepository categoriaRepository;

	public GastoService(GastoRepository gastoRepository, UsuarioRepository usuarioRepository,
			CategoriaRepository categoriaRepository) {
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

	public Gasto findById(Integer id) {
		return gastoRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Gasto de ID: " + id + " não encontrado!"));
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
				gasto.getCategoria() != null ? gasto.getCategoria().getNome() : null,
				gasto.getUsuario() != null ? gasto.getUsuario().getId() : null);
	}

}
