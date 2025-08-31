package com.minhaCarteira.crud.domain.gasto;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.minhaCarteira.crud.domain.categoria.Categoria;
import com.minhaCarteira.crud.domain.usuario.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "gasto", schema = "dbo")
@Builder
public class Gasto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "valor", nullable = false)
    private float valor;

    @Column(name = "descricao", nullable = false, length = 200)
    private String descricao;

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "data", nullable = false)
    private LocalDateTime data;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
