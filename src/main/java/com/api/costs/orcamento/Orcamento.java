package com.api.costs.orcamento;

import com.api.costs.orcamento.DTO.DadosAtulizarOrcamento;
import com.api.costs.orcamento.DTO.DadosCadastroOrcamento;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.time.LocalDate;

@Table(name = "tb_orcamento")
@Entity(name = "orcamentos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Orcamento {

    public Orcamento(DadosCadastroOrcamento dados) {
        this.debitado = true;
        this.nome = dados.nome();
        this.categoria = dados.categoria();
        this.valor = dados.valor();
        this.status = dados.status();
        this.dataCriacao = dados.dataCriacao();
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Double valor;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDate dataCriacao;
    private boolean debitado;


    public void atualizarInformacoes(@Valid DadosAtulizarOrcamento dados) {
        if (dados.nome() != null){
           this.nome = dados.nome();
        }

        if(dados.valor() != null){
            this.valor = dados.valor();
        }

        if(dados.status() != null){
            this.status = dados.status();
        }
    }

    public void debitar() {
        this.debitado = false;
    }

    public void registar() {
        this.debitado = true;
    }
}
