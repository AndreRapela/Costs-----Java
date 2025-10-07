package com.api.costs.controllers;

import com.api.costs.usuario.DTOs.DadosAtualizarUsuario;
import com.api.costs.usuario.DTOs.DadosCadastroUsuario;
import com.api.costs.usuario.Usuario;
import com.api.costs.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/registro")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Operation(summary = "Cadastrar",
            description = "Registra um novo usuário no sistema"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "um ou mais campos inválidos")
    })
    @Transactional
    @PostMapping
    public ResponseEntity<Usuario> cadastrarUsuario (@RequestBody @Valid DadosCadastroUsuario dados){
        Usuario usuario = repository.save(new Usuario(dados));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{login}").buildAndExpand(dados.login()).toUri();

        return ResponseEntity.created(uri).body(usuario);
    }

    @Operation(summary = "Alterar Senha",
            description = "Altera a senha de um usuário cadastrado"
    )
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200" , description = "senha atualizada com sucesso!"),
        @ApiResponse(responseCode = "404" , description = "usuário não encontrado!")
    }
    )
    @Transactional
    @PutMapping
    public ResponseEntity<DadosCadastroUsuario> alterarSenha (@RequestBody @Valid DadosAtualizarUsuario dados){
        Usuario usuario = repository.getReferenceById(dados.id());
        usuario.setSenha(dados.senha());
        return ResponseEntity.ok(new DadosCadastroUsuario(usuario));
    }

    @Operation(summary = "Excluir usuário",
                description = "Exclui permanentemente um usuário no sistema")
    @ApiResponse(responseCode = "204", description = "Conteúdo não encontrado")
    @Transactional
    @DeleteMapping
    public ResponseEntity<Void> excluirUsuario (@PathVariable Long id){
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
