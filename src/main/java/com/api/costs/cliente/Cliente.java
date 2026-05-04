package com.api.costs.cliente;

import com.api.costs.cliente.DTO.DadosCadastroCliente;
import com.api.costs.cliente.DTO.DadosCadastroClienteAdmin;
import com.api.costs.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Table(name = "tb_clientes", uniqueConstraints = {
        @UniqueConstraint(name = "uk_cliente_usuario_email", columnNames = {"usuario_id", "email"})
})
@Entity(name = "Clientes")
@SQLRestriction("ativo = true")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

    public Cliente(DadosCadastroCliente cliente){
        this.nome = cliente.nome();
        this.email = cliente.email();
        this.telefone = cliente.telefone();
    }

    public Cliente(DadosCadastroClienteAdmin cliente){
        this.nome = cliente.nome();
        this.email = cliente.email();
        this.telefone = cliente.telefone();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean ativo = true;
    private String nome;
    private String email;
    private String telefone;

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

    public void atualizarInformacoes (){}

    public void excluir (){this.ativo=false;}

}
