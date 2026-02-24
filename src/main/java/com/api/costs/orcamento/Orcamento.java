package com.api.costs.orcamento;

import com.api.costs.orcamento.DTO.DadosAtulizarOrcamento;
import com.api.costs.orcamento.DTO.DadosCadastroOrcamento;
import com.api.costs.orcamento.DTO.DadosCadastroOrcamentoAdmin;
import com.api.costs.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "tb_orcamento")
@Entity(name = "orcamentos")
@SQLRestriction("ativo=true")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orcamento {

    public Orcamento(DadosCadastroOrcamentoAdmin dados) {
        this.nome = dados.nome();
        this.categoria = dados.categoria();
        this.valor = dados.valor();
        this.status = dados.status();
    }

    public Orcamento(DadosCadastroOrcamento dados) {
        this.nome = dados.nome();
        this.categoria = dados.categoria();
        this.valor = dados.valor();
        this.status = dados.status();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @Enumerated(EnumType.STRING)
    private Status status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    private boolean ativo = true;

    @Column(name = "data_alteracao")
    @LastModifiedDate
    private LocalDateTime dataAlteracao;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false, updatable = false)
    @JsonIgnore
    private Usuario usuario;

    public void atualizarInformacoes(@Valid DadosAtulizarOrcamento dados) {

        if (dados.nome() != null) {
            this.nome = dados.nome();
        }

        if (dados.valor() != null) {
            this.valor = dados.valor();
        }

        if (dados.status() != null) {
            this.status = dados.status();
        }

    }

}
