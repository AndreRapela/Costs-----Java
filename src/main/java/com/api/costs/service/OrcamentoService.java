package com.api.costs.service;

import com.api.costs.orcamento.DTO.DadosAtulizarOrcamento;
import com.api.costs.orcamento.DTO.DadosCadastroOrcamento;
import com.api.costs.orcamento.Orcamento;
import com.api.costs.repository.OrcamentoRepository;
import com.api.costs.repository.UsuarioRepository;
import com.api.costs.usuario.Usuario;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OrcamentoService {

    @Autowired
    private OrcamentoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;


    @Transactional
    public Orcamento cadastrarOrcamentosPorUsuario (DadosCadastroOrcamento dados, Authentication authentication){

        Orcamento orcamento = new Orcamento(dados);
        orcamento.setUsuario(usuarioService.getUsuarioLogado(authentication));

        return repository.save(orcamento);
    }


    @Transactional
    public Orcamento cadastrarOrcamentos (DadosCadastroOrcamento dados){
        Orcamento orcamento = new Orcamento(dados);
        orcamento.setUsuario(usuarioRepository.getReferenceById(dados.usuarioId()));
        return repository.save(orcamento);
    }


    public Page<DadosCadastroOrcamento> listarOrcamentosPorUsuario(Authentication authentication, Pageable pageable){
        return repository.findByUsuario(usuarioService.getUsuarioLogado(authentication),pageable).map(DadosCadastroOrcamento::new);
    }


    public Page<DadosCadastroOrcamento> listarOrcamentos(Pageable page){
        return repository.findAll(page).map(DadosCadastroOrcamento::new);
    }


    public DadosCadastroOrcamento buscarOrcamentoPorId(Long id){
        return new DadosCadastroOrcamento(repository.getReferenceById(id));
    }


    public Page<DadosCadastroOrcamento> buscarOrcamentoPorNomePorUsuario(Authentication authentication, String nome, Pageable page ){
        return repository.findByUsuarioAndNomeContaining(usuarioService.getUsuarioLogado(authentication),nome,page).map(DadosCadastroOrcamento::new);
    }


    public Page<DadosCadastroOrcamento> buscarOrcamentoPorNome(String nome, Pageable page){
        return repository.findByNome(nome,page).map(DadosCadastroOrcamento::new);
    }


    @Transactional
    public DadosCadastroOrcamento atualizarOrcamento(DadosAtulizarOrcamento dados){
        Orcamento orcamento = repository.getReferenceById(dados.id());
        orcamento.atualizarInformacoes(dados);
        return new DadosCadastroOrcamento(orcamento);
    }


    @Transactional
    public DadosCadastroOrcamento atulizarOrcamentoPorUsuario(Authentication authentication, DadosAtulizarOrcamento dados){
        Orcamento orcamento = repository.findByUsuarioAndId(usuarioService.getUsuarioLogado(authentication),dados.id())
                .orElseThrow(EntityNotFoundException::new);
        orcamento.atualizarInformacoes(dados);
        return new DadosCadastroOrcamento(orcamento);
    }


    @Transactional
    public void excluirOrcamento(Long id){
        repository.deleteById(id);
    }


    @Transactional
    public void excluirOrcamentoPorUsuario (Authentication authentication, Long id){
        Usuario usuario = usuarioService.getUsuarioLogado(authentication);

        Orcamento orcamento = repository.findByUsuarioAndId(usuario,id).orElseThrow(EntityNotFoundException::new);

        repository.delete(orcamento);
    }

}
