package com.api.costs.controllers;

import com.api.costs.orcamento.DTO.DadosAtulizarOrcamento;
import com.api.costs.orcamento.DTO.DadosCadastroOrcamento;
import com.api.costs.orcamento.Orcamento;
import com.api.costs.repository.OrcamentoRepository;
import com.api.costs.service.OrcamentoService;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;

@RestController
@RequestMapping("/costs")
@Tag(name = "COSTS" , description = "Projeto de arquivamento e manutenção de orçamentos para controle financeiro")
public class OrcamentoController {

    @Autowired
    private OrcamentoService service;


    @Operation(summary = " CADASTRO ORÇAMENTO POR USUÁRIO",
            description = "Cadastra um orçamento do usuário atual passando:" +
                    " String nome , Double valor, Status status(ENUM) , Categoria categoria(Enum) ")
    @ApiResponses (value = {
            @ApiResponse(responseCode = "201", description = "Orçamento cadastrado com sucesso."),
            @ApiResponse(responseCode = "400", description = "um ou mais campo(s)  inválido(s)."),
            @ApiResponse(responseCode = "422", description = "Dados válidos em sintaxe, mas inconsistentes (ex: status inexistente, valor negativo)."),
            @ApiResponse(responseCode = "409", description = "Conflito: orçamento com dados duplicados. Já existente."),
    })
    @PostMapping
    public ResponseEntity<Orcamento> cadastrarOrcamentosPorUsuario(@RequestBody @Valid DadosCadastroOrcamento dados, Authentication authentication){
        Orcamento orcamento = service.cadastrarOrcamentosPorUsuario(dados, authentication);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(orcamento.getId()).toUri();
        return ResponseEntity.created(uri).body(orcamento);
    }


    @Operation(summary = "CADASTRAR USUÁRIO",
            description = "Cadastra um orçamento do usuário atual passando: " +
                    "String nome, Double valor, Status status(ENUM), Categoria categoria(ENUM)")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "201", description = "Orçamento cadastrado com sucesso."),
            @ApiResponse(responseCode = "400", description = "um ou mais campo(s)  inválido(s)."),
            @ApiResponse(responseCode = "422", description = "Dados válidos em sintaxe, mas inconsistentes (ex: status inexistente, valor negativo)."),
            @ApiResponse(responseCode = "409", description = "Conflito: orçamento com dados duplicados já existe.")
    })
    @PostMapping("/admin")
    public ResponseEntity<Orcamento> cadastrarOrcamentos(@RequestBody @Valid DadosCadastroOrcamento dados){
        Orcamento orcamento =  service.cadastrarOrcamentos(dados);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(orcamento.getId()).toUri();
        return  ResponseEntity.created(uri).body(orcamento);
    }


    @Operation(summary = "BUSCA COMPLETA DE ORÇAMENTOS",
            description = "Retornar toda a lista de orçamentos , devidamente paginada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de lista de orçamentos retornada com sucesso."),
            @ApiResponse(responseCode = "404", description = "lista não encontrada.")
    })
    @GetMapping("/admin")
    public ResponseEntity<Page<DadosCadastroOrcamento>> listarOrcamentos (Pageable page){
        return ResponseEntity.ok(service.listarOrcamentos(page));
    }


    @Operation(summary = "BUSCA DE ORÇAMENTOS POR USUÁRIO",
                description = "Retorna a lista completa de orçamentos paginada do priprio usuário")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "Página de lista do usuário retornada com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada.")
    })
    @GetMapping
    public ResponseEntity<Page<DadosCadastroOrcamento>> listarOrcamentoPorUsuario (Authentication authentication, Pageable page){
        return ResponseEntity.ok(service.listarOrcamentosPorUsuario(authentication, page));
    }


    @Operation(summary = "BUSCA POR ID",
            description = "Retorna um orçamento específico passando o id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagina de orçamentos buscado por id encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Orçamento não encontrado.")
    })
    @GetMapping("/admin/{id}")
    public ResponseEntity<DadosCadastroOrcamento> buscarOrcamentoPorId(@PathVariable Long id){
        return ResponseEntity.ok(service.buscarOrcamentoPorId(id));
    }


    @Operation(summary = "BUSCAR POR NOME POR USUÁRIO",
            description = "Retorna uma lista paginada de orçamentos com o nome buscado por paramentros do proprio usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Página de orçamento buscado por nome encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Página não encontrada")
    })
    @GetMapping("/find")
    public ResponseEntity<Page<DadosCadastroOrcamento>> buscarOrcamentoPorUsuarioPorNome(Authentication authentication,@RequestParam String nome, Pageable page){
        return ResponseEntity.ok(service.buscarOrcamentoPorNomePorUsuario(authentication,nome,page));
    }


    @Operation(summary = "BUSCA POR NOME",
            description = "Retorna toda a lista paginada de orçamentos com o nome buscado por parametro")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Página de lista de orçamento buscada por nome retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Página não encontrada")
    })
   @GetMapping("admin/find")
    public  ResponseEntity<Page<DadosCadastroOrcamento>> buscarOrcamentoPorNome(@RequestParam String nome, Pageable page){
        return ResponseEntity.ok(service.buscarOrcamentoPorNome(nome,page));
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
            @ApiResponse(responseCode = "400", description = "um ou mais campo(s) invalido(s)."),
            @ApiResponse(responseCode = "404", description = "usuário não encontrado."),
            @ApiResponse(responseCode = "422", description = "Dados válidos em sintaxe, mas inconsistentes."),
    })
    @PutMapping("/admin")
    public ResponseEntity<DadosCadastroOrcamento> atualizarOrcamento (@RequestBody @Valid DadosAtulizarOrcamento dados) {
        return ResponseEntity.ok(service.atualizarOrcamento(dados));
    }


    @Operation(summary = "EDIÇÃO DE ORÇAMENTO POR USUÁRIO", description = "Altera um orçamento existente do próprio usuário passando o id Long" +
            "Apenas é possível alterar o String nome, Double valor e o Status status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orçamento atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "um ou mais campo(s) inválido(s)."),
            @ApiResponse(responseCode = "422", description = "Dados válidos em sintaxe, mas inconsistentes."),
    })
    @PutMapping
    public ResponseEntity<DadosCadastroOrcamento> atualizarOrcamentoPorUsuario(Authentication authentication ,@RequestBody @Valid DadosAtulizarOrcamento dados){
        return ResponseEntity.ok(service.atulizarOrcamentoPorUsuario(authentication,dados));
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
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> excluirOrcamento (@PathVariable long id){
        service.excluirOrcamento(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "EXCLUSÃO DE ORÇAMENTO PELO PRORPIO USUÁRIO",
            description = "Excluir um orçamento do proprio usuário passando o id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Nenhum conteúdo encontrado, sendo bem sucedido"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirOrcamentoPorUsuario(Authentication authentication, @PathVariable Long id){
        service.excluirOrcamentoPorUsuario(authentication, id);
        return ResponseEntity.noContent().build();
    }


//    @DeleteMapping("debitado/{id}")
//    @Transactional
//    public void debitar (@PathVariable long id){
//        var orcamento = repository.getReferenceById(id);
//        orcamento.debitar();
//    }



}
