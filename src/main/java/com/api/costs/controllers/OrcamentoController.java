package com.api.costs.controllers;

import com.api.costs.orcamento.DTO.DadosAtulizarOrcamento;
import com.api.costs.orcamento.DTO.DadosCadastroOrcamento;
import com.api.costs.orcamento.Orcamento;
import com.api.costs.orcamento.repository.OrcamentoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/costs")
public class OrcamentoController {

    @Autowired
    private OrcamentoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity<Orcamento> cadastrar(@RequestBody @Valid DadosCadastroOrcamento dados){
        Orcamento orcamento = repository.save(new Orcamento(dados));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(orcamento.getId()).toUri();
        return ResponseEntity.created(uri).body(orcamento);
    }

    @GetMapping
    public List<DadosCadastroOrcamento> listarOrcamento (){
        return repository.findAll().stream().map(DadosCadastroOrcamento::new).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosCadastroOrcamento> orcamentoPorId(@PathVariable Long id){
        return ResponseEntity.ok(new DadosCadastroOrcamento(repository.getReferenceById(id)));
    }

    @GetMapping("/debitado")
    public List<DadosCadastroOrcamento> listarOrcamentosNaoDebitados () {
        return  repository.findAllByDebitadoTrue().stream().map(DadosCadastroOrcamento::new).toList();
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosCadastroOrcamento> atualizarOrcamento (@RequestBody @Valid DadosAtulizarOrcamento dados) {
        var orcamento = repository.getReferenceById(dados.id());
        orcamento.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosCadastroOrcamento(orcamento));
    }

    @PutMapping("debitado/{id}")
    @Transactional
    public void registrar (@PathVariable long id){
        var orcamento = repository.getReferenceById(id);
        orcamento.registar();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluirOrcamento (@PathVariable long id){
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("debitado/{id}")
    @Transactional
    public void debitar (@PathVariable long id){
        var orcamento = repository.getReferenceById(id);
        orcamento.debitar();
    }
    
}
