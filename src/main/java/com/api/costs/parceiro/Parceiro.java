package com.api.costs.parceiro;

import com.api.costs.parceiro.DTO.DadosAtualizarParceiro;
import com.api.costs.parceiro.DTO.DadosCadastroParceiro;
import com.api.costs.parceiro.DTO.DadosCadastroParceiroAdmin;
import com.api.costs.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Table(name = "tb_parceiros", uniqueConstraints = {
        @UniqueConstraint(name = "uk_parceiro_usuario_email", columnNames = {"usuario_id", "email"})
})
@Entity(name = "Parceiros")
@SQLRestriction("ativo = true")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Parceiro {

    public Parceiro(DadosCadastroParceiro parceiro){
        this.nome = parceiro.nome();
        this.email = parceiro.email();
        this.telefone = parceiro.telefone();
        this.categoria = parceiro.categoria();
    }

    public Parceiro(DadosCadastroParceiroAdmin parceiro){
        this.nome = parceiro.nome();
        this.email = parceiro.email();
        this.telefone = parceiro.telefone();
        this.categoria = parceiro.categoria();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean ativo = true;
    private String nome;
    private String email;
    private String telefone;
    private Categoria categoria;

    @CreatedDate
    @Column(nullable = false,updatable = false)
    private LocalDateTime dataCriacao;

    @LastModifiedDate
    @Column(name="data_alteracao")
    private LocalDateTime dataAlteracao;


    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "usuario_id" ,nullable = false,updatable = false)
    @JsonIgnore
    private Usuario usuario;

    public void atualizarInformacoes (@Valid DadosAtualizarParceiro dados){
        if(dados.email() != null){
            this.email = dados.email();
        }
        if (dados.telefone() != null){
            this.telefone = dados.telefone();
        }
    }

    public void excluir (){this.ativo=false;}

}
