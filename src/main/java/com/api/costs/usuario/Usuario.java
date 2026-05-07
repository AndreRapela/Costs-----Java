package com.api.costs.usuario;

import com.api.costs.parceiro.Parceiro;
import com.api.costs.infra.Role;
import com.api.costs.orcamento.Orcamento;
import com.api.costs.usuario.DTOs.DadosAtualizarUsuario;
import com.api.costs.usuario.DTOs.DadosCadastroUsuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Table(name = "tb_usuario")
@Entity(name = "usuario")
@SQLRestriction("ativo=true")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Usuario implements UserDetails {

    public Usuario(DadosCadastroUsuario dados) {
        this.login = dados.login();
        this.senha = dados.senha();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 8, max = 50)
    private String login;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @Size(min = 8, max = 20)
    private String senha;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private List<Orcamento> orcamentos = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private List<Parceiro> parceiros = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    private boolean ativo = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }

    public void atualizarSenha(DadosAtualizarUsuario dados) {
        this.senha = dados.senha();
    }

}
