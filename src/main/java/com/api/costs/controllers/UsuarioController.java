package com.api.costs.controllers;

import com.api.costs.service.UsuarioService;
import com.api.costs.usuario.DTOs.DadosAtualizarUsuario;
import com.api.costs.usuario.DTOs.DadosCadastroUsuario;
import com.api.costs.usuario.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/registro")
public class UsuarioController {

    @Autowired
    private UsuarioService service;


    @Operation(summary = "CADASTRAR NOVO USUÁRIO",
            description = "Registra um novo usuário no sistema"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Um ou mais campos inválidos"),
            @ApiResponse(responseCode = "422", description = "Dados válidos em sintaxe, mas inconsistentes (ex: status inexistente, valor negativo)"),
            @ApiResponse(responseCode = "409",description = "Conflito: Esse usuário já existe")
    })
    @Transactional
    @PostMapping
    public ResponseEntity<Usuario> cadastrarUsuario (@RequestBody @Valid DadosCadastroUsuario dados){
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{login}").buildAndExpand(dados.login()).toUri();

        return ResponseEntity.created(uri).body(service.cadastrarUsuario(dados));
    }


    @Operation(summary = "LISTAR TODOS OS USUÁRIOS",
            description = "Listar todos os usuários ")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista encontrada com sucesso!"),
        @ApiResponse(responseCode = "404", description = "Lista de usuários não encontrada.")
    })
    @GetMapping("/admin")
    public ResponseEntity<Page<DadosCadastroUsuario>> buscarUsuarios (Pageable page){
        return ResponseEntity.ok(service.buscarUsuarios(page));
    }


    @Operation(summary = "BUSCAR USUÁRIOS POR NOME",
            description = "Lista todos os usuários que contenham o nome passado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "lista de usuários encontrada com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Lista de usuários não encontrada!")
    })
    @GetMapping("/admin/find")
    public ResponseEntity<Page<DadosCadastroUsuario>> buscarUsuariosPorLogin (Pageable page,@RequestParam String nome){
        return  ResponseEntity.ok(service.buscarUsuarioPorNome(page,nome));
    }


    @Operation(summary = "BUSCAR USUÁRIOS POR ID",
            description = "Busca o usuário que o id específico se refere")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Falha ao encontrar o usuário!")
    })
    @GetMapping("/admin/{id}")
    public ResponseEntity<DadosCadastroUsuario> buscarUsuarioPorId (@PathVariable Long id){
        return ResponseEntity.ok(service.buscarPorId(id));
    }


    @Operation(summary = "ALTERAR SENHA",
            description = "Altera a senha de um usuário cadastrado"
    )
    @ApiResponses( value = {
        @ApiResponse(responseCode = "200" , description = "senha atualizada com sucesso!"),
        @ApiResponse(responseCode = "404" , description = "usuário não encontrado!"),
        @ApiResponse(responseCode = "422", description = "Dados válidos, mas inconsistentes."),
        @ApiResponse(responseCode = "400", description = "Um ou mais campo(s) inválido(s)")
    }
    )
    @PutMapping("/admin")
    public ResponseEntity<DadosCadastroUsuario> alterarSenha (@RequestBody @Valid DadosAtualizarUsuario dados){
        return ResponseEntity.ok(service.atualizarSenha(dados));
    }


    @Operation(summary= "ATUALIZAR SENHA DO USUÁRIO",
            description = "Atualizar Senha do próprio do usuário")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Senha atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Um ou mais campo(s) inválido(s)"),
            @ApiResponse(responseCode = "422", description = "Dados válidos em sintaxe, mas inconsistentes"),
    })
    @PutMapping
    public ResponseEntity<DadosCadastroUsuario> alterarSenhaDoUsuario
            (Authentication authentication, @RequestBody @Valid DadosAtualizarUsuario dados){
        return ResponseEntity.ok(service.atualizarSenhaDoUsuario(authentication,dados));
    }


    @Operation(summary = "EXCLUIR USUÁRIO",
            description = "Exclui permanentemente um usuário no sistema")
    @ApiResponse(responseCode = "204", description = "Usuários não encontrado")
    @Transactional
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> excluirUsuario (@PathVariable Long id){
        service.excluirUsuario(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "EXCLUIR PRÓPRIA CONTA",
        description = "exclui o proprio usuário permanentemente")
    @ApiResponse(responseCode = "204", description = "Usuário não encontrado")
    @DeleteMapping
    public ResponseEntity<Void> excluirUsuarioSelf(Authentication authentication){
        service.excluirUsuarioSelf(authentication);
        return ResponseEntity.noContent().build();
    }
}
