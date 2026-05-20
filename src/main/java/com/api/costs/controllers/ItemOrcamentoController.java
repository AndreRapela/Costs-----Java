package com.api.costs.controllers;

import com.api.costs.itemOrcamento.DTO.*;
import com.api.costs.itemOrcamento.ItemOrcamento;
import com.api.costs.service.ItemOrcamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/itemOrcamento")
@Tag(name = "ITEM Orçamento" , description = "Projeto de arquivamento e manutenção de orçamentos para controle financeiro")
public class ItemOrcamentoController {

    @Autowired
    private ItemOrcamentoService service;


    @Operation(summary = " CADASTRO ITEM DE ORÇAMENTO POR USUÁRIO",
            description = "Cadastra um item  deorçamento do usuário atual passando:" +
                    " String nome , Double valor, Status status(ENUM) , Categoria categoria(Enum) ")
    @ApiResponses (value = {
            @ApiResponse(responseCode = "201", description = "Item do orçamento cadastrado com sucesso."),
            @ApiResponse(responseCode = "400", description = "um ou mais campo(s)  inválido(s)."),
            @ApiResponse(responseCode = "422", description = "Dados válidos em sintaxe, mas inconsistentes (ex: status inexistente, valor negativo)."),
            @ApiResponse(responseCode = "409", description = "Conflito: orçamento com dados duplicados. Já existente."),
    })
    @PostMapping
    public ResponseEntity<DadosListarItemOrcamento> cadastrarItemOrcamentosPorUsuario(@RequestBody @Valid DadosCadastroItemOrcamento dados, Authentication authentication){
        ItemOrcamento itemOrcamento = service.cadastrarItemOrcamentosPorUsuario(dados, authentication);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(itemOrcamento.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListarItemOrcamento(itemOrcamento));
    }


    @Operation(summary = "CADASTRAR ORÇAMENTO",
            description = "Cadastra um orçamento : " +
                    "String nome, Double valor, Status status(ENUM), Categoria categoria(ENUM)")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "201", description = "Orçamento cadastrado com sucesso."),
            @ApiResponse(responseCode = "400", description = "um ou mais campo(s)  inválido(s)."),
            @ApiResponse(responseCode = "422", description = "Dados válidos em sintaxe, mas inconsistentes (ex: status inexistente, valor negativo)."),
            @ApiResponse(responseCode = "409", description = "Conflito: orçamento com dados duplicados já existe.")
    })
    @PostMapping("/admin")
    public ResponseEntity<DadosListarItemOrcamentoAdmin> cadastrarItemOrcamentos(@RequestBody @Valid DadosCadastroItemOrcamentoAdmin dados){
        ItemOrcamento itemOrcamento = service.cadastrarItemOrcamentos(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(itemOrcamento.getId()).toUri();
        return  ResponseEntity.created(uri).body(new DadosListarItemOrcamentoAdmin(itemOrcamento));
    }


    @Operation(summary = "BUSCA COMPLETA DE ORÇAMENTOS",
            description = "Retornar toda a lista de orçamentos , devidamente paginada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de lista de orçamentos retornada com sucesso."),
            @ApiResponse(responseCode = "404", description = "lista não encontrada.")
    })
    @GetMapping("/admin")
    public ResponseEntity<Page<DadosListarItemOrcamentoAdmin>> listarItemOrcamentos (Pageable page){
        return ResponseEntity.ok(service.listarItemOrcamentos(page));
    }


    @Operation(summary = "BUSCA DE ORÇAMENTOS POR USUÁRIO",
                description = "Retorna a lista completa de orçamentos paginada do priprio usuário")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "Página de lista do usuário retornada com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada.")
    })
    @GetMapping
    public ResponseEntity<Page<DadosListarItemOrcamento>> listarItemOrcamentoPorUsuario (Authentication authentication, Pageable page){
        return ResponseEntity.ok(service.listarItemOrcamentosPorUsuario(authentication, page));
    }


    @Operation(summary = "BUSCA POR ID",
            description = "Retorna um orçamento específico passando o id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagina de orçamentos buscado por id encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Orçamento não encontrado.")
    })
    @GetMapping("/admin/{id}")
    public ResponseEntity<DadosListarItemOrcamentoAdmin> buscarItemOrcamentoPorId(@PathVariable Long id){
        return ResponseEntity.ok(service.buscarItemOrcamentoPorId(id));
    }


    @Operation(summary = "BUSCAR POR NOME POR USUÁRIO",
            description = "Retorna uma lista paginada de orçamentos com o nome buscado por paramentros do proprio usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Página de orçamento buscado por nome encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Página não encontrada")
    })
    @GetMapping("/find")
    public ResponseEntity<Page<DadosListarItemOrcamento>> buscarItemOrcamentoPorUsuarioPorNome(Authentication authentication, @RequestParam String nome, Pageable page){
        return ResponseEntity.ok(service.buscarItemOrcamentoPorNomePorUsuario(authentication,nome,page));
    }


    @Operation(summary = "BUSCA POR NOME",
            description = "Retorna toda a lista paginada de orçamentos com o nome buscado por parametro")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Página de lista de orçamento buscada por nome retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Página não encontrada")
    })
   @GetMapping("admin/find")
    public  ResponseEntity<Page<DadosListarItemOrcamentoAdmin>> buscarItemOrcamentoPorNome(@RequestParam String nome, Pageable page){
        return ResponseEntity.ok(service.buscarItemOrcamentoPorNome(nome,page));
    }


    @Operation(summary = "EDIÇÃO DE ORÇAMENTO",
            description = "Altera um orçamento existente . " +
            "Apenas é possível alterar o String nome, Double valor e o Status status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" ,description = "Orçamento atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "um ou mais campo(s) invalido(s)."),
            @ApiResponse(responseCode = "404", description = "usuário não encontrado."),
            @ApiResponse(responseCode = "422", description = "Dados válidos em sintaxe, mas inconsistentes."),
    })
    @PutMapping("/admin")
    public ResponseEntity<DadosListarItemOrcamentoAdmin> atualizarItemOrcamento (@RequestBody @Valid DadosAtulizarItemOrcamento dados) {
        return ResponseEntity.ok(service.atualizarItemOrcamento(dados));
    }


    @Operation(summary = "EDIÇÃO DE ORÇAMENTO POR USUÁRIO", description = "Altera um orçamento existente do próprio usuário passando o id Long" +
            "Apenas é possível alterar o String nome, Double valor e o Status status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orçamento atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "um ou mais campo(s) inválido(s)."),
            @ApiResponse(responseCode = "422", description = "Dados válidos em sintaxe, mas inconsistentes."),
    })
    @PutMapping
    public ResponseEntity<DadosListarItemOrcamento> atualizarItemOrcamentoPorUsuario(Authentication authentication , @RequestBody @Valid DadosAtulizarItemOrcamento dados){
        return ResponseEntity.ok(service.atulizarItemOrcamentoPorUsuario(authentication,dados));
    }


    @Operation(summary = "EXCLUSÃO DE ORÇAMENTO",
            description = "Exclui um orçamento existente passando o id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204" , description = "Nenhum conteúdo encontrado, sendo bem sucedido ou não"),
    })
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> excluirItemOrcamento (@PathVariable long id){
        service.excluirItemOrcamento(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "EXCLUSÃO DE ORÇAMENTO PELO PRORPIO USUÁRIO",
            description = "Excluir um orçamento do proprio usuário passando o id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Nenhum conteúdo encontrado, sendo bem sucedido"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirItemOrcamentoPorUsuario(Authentication authentication, @PathVariable Long id){
        service.excluirItemOrcamentoPorUsuario(authentication, id);
        return ResponseEntity.noContent().build();
    }


}
