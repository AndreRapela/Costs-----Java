package com.api.costs.controllers;

import com.api.costs.parceiro.DTO.*;
import com.api.costs.parceiro.Parceiro;
import com.api.costs.service.ParceiroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/parceiro")
public class ParceiroController {

    @Autowired
    private ParceiroService service;

    @Operation(summary = "CADASTRAR PARCEIRO",
            description ="Cadastra um parceiro:" +
                    " String nome, String email, String Telefone,Categoria categoria,Long usuarioId " )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Parceiro cadastrado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Um ou mais campo(s) invalidos" ),
            @ApiResponse(responseCode = "409", description ="Dados corretos em sintaxe,mas inconsistentes (ex: categoria inesistinte)" ),
            @ApiResponse(responseCode = "422", description = "Conflito: Parceiro com dados duplicados, já existe no banco.")
    })
    @PostMapping("/admin")
    private ResponseEntity<DadosListarParceiroAdmin> cadastrarParceiro (@RequestBody @Valid DadosCadastroParceiroAdmin dados){
        Parceiro parceiro = service.cadastrarParceiro(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(parceiro.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListarParceiroAdmin(parceiro));
    }


    @Operation(summary = "CADASTRAR PARCEIRO POR USUÁRIO",
            description = "Cadastra um parceiro do usuário logado" +
                    " String nome, String email, String Telefone,Categoria categoria" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Parceiro cadastrado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Um ou mais campo(s) invalidos" ),
            @ApiResponse(responseCode = "409", description ="Dados corretos em sintaxe,mas inconsistentes (ex: categoria inesistinte)" ),
            @ApiResponse(responseCode = "422", description = "Conflito: Parceiro com dados duplicados, já existe no banco.")
    })
    @PostMapping
    private ResponseEntity<DadosListarParceiro> cadastrarParceiroPorUsuario(@RequestBody @Valid DadosCadastroParceiro dados, Authentication authentication){
        Parceiro parceiro = service.cadastrarParceiroPorUsuario(dados,authentication);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(parceiro.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListarParceiro(parceiro));
    }


    @Operation(summary = "lISTAR PARCEIROS",
            description = "Retorna toda a lista de Parceiros")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de parceiros  retornada com sucesso."),
            @ApiResponse(responseCode = "404", description = "lista não encontrada.")
    })
    @GetMapping("/admin")
    private ResponseEntity<Page<DadosListarParceiroAdmin>> buscarParceiros (Pageable page){
        return ResponseEntity.ok(service.listarParceiro(page));
    }


    @Operation(summary = "lISTAR PARCEIROS POR USUÁRIO",
            description = "Retorna toda a lista de Parceiros")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de parceiros retornada com sucesso."),
            @ApiResponse(responseCode = "404", description = "lista não encontrada.")
    })
    @GetMapping
    private ResponseEntity<Page<DadosListarParceiro>> buscarParceiroPorUsuario (Pageable page, Authentication authentication){
        return ResponseEntity.ok(service.listarParceiroPorUsuario(page,authentication));
    }


    @Operation(summary = "BUSCA DE PARCEIRO POR ID",
            description = "Retorna Parceiro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "parceiro retornado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Parceiro não encontrado.")
    })
    @GetMapping("/admin/{id}")
    private ResponseEntity<DadosListarParceiroAdmin> buscarParceiroPorId (@PathVariable Long id){
        return ResponseEntity.ok(service.buscarParceiroPorId(id));
    }


    @Operation(summary = "lISTAR PARCEIROS POR NOME POR USUARIO",
            description = "Retorna Parceiro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "parceiro retornado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Parceiro não encontrado.")
    })
    @GetMapping("/find")
    private  ResponseEntity<Page<DadosListarParceiro>> buscarParceiroPorNomePorUsuario (@RequestParam String nome, Pageable page,Authentication authentication){
        return ResponseEntity.ok(service.buscarParceiroPorNomePorUsuario(nome, page,authentication));
    }


    @Operation(summary = "lISTAR PARCEIROS POR NOME",
            description = "Retorna Parceiro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "parceiro retornado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Parceiro não encontrado.")
    })
    @GetMapping("/admin/find")
    private ResponseEntity<Page<DadosListarParceiroAdmin>> buscarParceiroPorNome (@RequestParam String nome, Pageable page){
        return ResponseEntity.ok(service.buscarParceirosPorNome(nome, page));
    }


    @Operation(summary = "EDIÇÃO DE PARCEIRO",
            description = "Altera um parceiro existente . ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" ,description = "Parceiro atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "um ou mais campo(s) invalido(s)."),
            @ApiResponse(responseCode = "404", description = "parceiro não encontrado."),
            @ApiResponse(responseCode = "422", description = "Dados válidos em sintaxe, mas inconsistentes."),
    })
    @PutMapping("/admin")
    private ResponseEntity<DadosListarParceiroAdmin> atualizarParceiro (@RequestBody @Valid DadosAtualizarParceiro dados){
        return ResponseEntity.ok(service.atualizarParceiro(dados));
    }


    @Operation(summary = "EDIÇÃO DE PARCEIRO",
            description = "Altera um parceiro existente . ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" ,description = "Parceiro atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "um ou mais campo(s) invalido(s)."),
            @ApiResponse(responseCode = "404", description = "parceiro não encontrado."),
            @ApiResponse(responseCode = "422", description = "Dados válidos em sintaxe, mas inconsistentes."),
    })
    @PutMapping
    private ResponseEntity<DadosListarParceiro> atualizarParceiroPorUsuario (@RequestBody @Valid DadosAtualizarParceiro dados ,Authentication authentication){
        return ResponseEntity.ok(service.atualizarParceiroPorUsuario(dados,authentication));
    }


    @Operation(summary = "EXCLUSÃO DE PARCEIRO",
            description = "Exclui um parceiro existente passando o id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204" , description = "Nenhum conteúdo encontrado, sendo bem sucedido ou não"),
    })
    @DeleteMapping("/admin")
    private ResponseEntity<Void> excluirParceiro (@PathVariable Long id){
        service.excluirParceiro(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "EXCLUSÃO DE PARCEIRO PELO PROPRIO USUÁRIO",
            description = "Exclui um parceiro existente passando o id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204" , description = "Nenhum conteúdo encontrado, sendo bem sucedido ou não"),
    })
    @DeleteMapping
    private ResponseEntity<Void> excluirParceiroPorUsuario (@PathVariable Long id, Authentication authentication){
        service.excluirParceiroPorUsuario(authentication, id);
        return ResponseEntity.noContent().build();
    }
}
