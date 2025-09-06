package com.api.costs.controllers;

import com.api.costs.orcamento.DTO.DadosAtulizarOrcamento;
import com.api.costs.orcamento.DTO.DadosCadastroOrcamento;
import com.api.costs.orcamento.Orcamento;
import com.api.costs.orcamento.repository.OrcamentoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;

@RestController
@RequestMapping("/costs")
@Tag(name = "COSTS" , description = "Projeto de arquivamento e manutenção de orçamentos para controle financeiro")
public class OrcamentoController {

    @Autowired
    private OrcamentoRepository repository;

    @Operation(summary = " CADASTRO",
            description = "Cadastra um orçamento passando: String nome , Double valor, Status status(ENUM) , Categoria categoria(Enum) ")
    @ApiResponses (value = {
            @ApiResponse(responseCode = "200", description = "Orçamento cadastrado com sucesso."),
            @ApiResponse( responseCode = "400", description = "um ou mais campo(s)  inválido(s).")
    })
    @PostMapping
    @Transactional
    public ResponseEntity<Orcamento> cadastrar(@RequestBody @Valid DadosCadastroOrcamento dados){
        Orcamento orcamento = repository.save(new Orcamento(dados));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(orcamento.getId()).toUri();
        return ResponseEntity.created(uri).body(orcamento);
    }

    @Operation(summary = "BUSCA COMPLETA",
            description = "Retornar toda a lista de orçamentos , devidamente paginada.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Página de lista de orçamentos retornada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada.")
    })
    @GetMapping
    public ResponseEntity<Page<DadosCadastroOrcamento>> listarOrcamento (Pageable page){
        return ResponseEntity.ok(repository.findAll(page).map(DadosCadastroOrcamento::new));
    }

    @Operation(summary = "BUSCA POR ID",
            description = "Retorna um orçamento específico passando o id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orçamento buscado por id encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Orçamento não encontrado.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DadosCadastroOrcamento> orcamentoPorId(@PathVariable Long id){
        return ResponseEntity.ok(new DadosCadastroOrcamento(repository.getReferenceById(id)));
    }

    @Operation(summary = "BUSCA POR NOME",
            description = "Retorna toda a lista paginada de orçamentos com o nome buscado por parametro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de lista de orçamento buscada por nome retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nome não encontrado.")
    })
    @GetMapping("/find")
    public  ResponseEntity<Page<DadosCadastroOrcamento>> orcamentoPorNome(@RequestParam String nome, Pageable page){
        return ResponseEntity.ok(repository.findByNome(nome,page).map(DadosCadastroOrcamento::new));
    }

//    @GetMapping("/debitado")
//    public List<DadosCadastroOrcamento> listarOrcamentosNaoDebitados () {
//        return  repository.findAllByDebitadoTrue().stream().map(DadosCadastroOrcamento::new).toList();
//    }

    @Operation(summary = "EDIÇÃO DE ORÇAMENTO",
            description = "Altera um orçamento existente passando o id Long. " +
            "Apenas é possível alterar o String nome, Double valor e o Status status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" ,description = "Orçamento atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "um ou mais campo(s) invalido(s).")
    })
    @PutMapping
    @Transactional
    public ResponseEntity<DadosCadastroOrcamento> atualizarOrcamento (@RequestBody @Valid DadosAtulizarOrcamento dados) {
        var orcamento = repository.getReferenceById(dados.id());
        orcamento.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosCadastroOrcamento(orcamento));
    }

//    @PutMapping("debitado/{id}")
//    @Transactional
//    public void registrar (@PathVariable long id){
//        var orcamento = repository.getReferenceById(id);
//        orcamento.registar();
//    }
    @Operation(summary = "EXCLUSÃO DE ORÇAMENTO",
            description = "Exclui um orçamento existente passando o id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204" , description = "Nenhum conteúdo encontrado, sendo bem sucedido ou não"),
    })
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluirOrcamento (@PathVariable long id){
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

//    @DeleteMapping("debitado/{id}")
//    @Transactional
//    public void debitar (@PathVariable long id){
//        var orcamento = repository.getReferenceById(id);
//        orcamento.debitar();
//    }
    
}
