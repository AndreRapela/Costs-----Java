package com.api.costs.usuário;

import jakarta.persistence.*;
import lombok.*;

@Table(name="tb_usuario")
@Entity(name="usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String login;
    private String senha;
}
