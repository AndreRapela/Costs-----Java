package com.api.costs.orcamento;

import com.api.costs.orcamento.DTO.DadosAtulizarOrcamento;
import com.api.costs.orcamento.DTO.DadosCadastroOrcamento;
import com.api.costs.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "tb_orcamento")
@Entity(name = "orcamentos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orcamento {

    public Orcamento(DadosCadastroOrcamento dados) {
        this.debitado = true;
        this.nome = dados.nome();
        this.categoria = dados.categoria();
        this.valor = dados.valor();
        this.status = dados.status();
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDate dataCriacao;
    private boolean debitado;

    @Column(name = "data_alteracao")
    private LocalDate dataAltercao;

    @ManyToOne
    @JoinColumn(name = "usuario_id" , nullable = false, updatable = false)
    private Usuario usuario;

    @PrePersist
    public void SetdataCriacao(){
        this.dataCriacao = LocalDate.now();
    }

    public void atualizarInformacoes(@Valid DadosAtulizarOrcamento dados) {
        int cont = 0;

        if (dados.nome() != null){
           this.nome = dados.nome();
           cont++;
        }

        if(dados.valor() != null){
            this.valor = dados.valor();
            cont++;
        }

        if(dados.status() != null){
            this.status = dados.status();
            cont++;
        }

        if(cont>0){
            this.dataAltercao = LocalDate.now();
        }


    }

    public void debitar() {
        this.debitado = false;
    }

    public void registar() {
        this.debitado = true;
    }
}
