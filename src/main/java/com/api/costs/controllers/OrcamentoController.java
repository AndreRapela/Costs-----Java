package com.api.costs.controllers;

import com.api.costs.orcamento.DTO.DadosAtulizarOrcamento;
import com.api.costs.orcamento.DTO.DadosCadastroOrcamento;
import com.api.costs.orcamento.Orcamento;
import com.api.costs.repository.OrcamentoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/costs")
public class OrcamentoController {

    @Autowired
    private OrcamentoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity<Orcamento> cadastrar(@RequestBody @Valid DadosCadastroOrcamento dados){
        return new ResponseEntity<>(repository.save(new Orcamento(dados)), HttpStatus.CREATED);
    }

    @GetMapping
    public List<DadosCadastroOrcamento> listarOrcamento (){
        return repository.findAll().stream().map(DadosCadastroOrcamento::new).toList();
    }

    @GetMapping("/debitado")
    public List<DadosCadastroOrcamento> listarOrcamentosNaoDebitados () {
        return  repository.findAllByDebitadoTrue().stream().map(DadosCadastroOrcamento::new).toList();
    }

    @PutMapping
    @Transactional
    public void atualizarOrcamento (@RequestBody @Valid DadosAtulizarOrcamento dados) {
        var orcamento = repository.getReferenceById(dados.id());
        orcamento.atualizarInformacoes(dados);
    }

    @PutMapping("debitado/{id}")
    @Transactional
    public void registrar (@PathVariable long id){
        var orcamento = repository.getReferenceById(id);
        orcamento.registar();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void excluirOrcamento (@PathVariable long id){
        repository.deleteById(id);
    }

    @DeleteMapping("debitado/{id}")
    @Transactional
    public void debitar (@PathVariable long id){
        var orcamento = repository.getReferenceById(id);
        orcamento.debitar();
    }
    
}
