package com.api.costs.service;

import com.api.costs.orcamento.DTO.DadosAtulizarOrcamento;
import com.api.costs.orcamento.DTO.DadosCadastroOrcamento;
import com.api.costs.orcamento.DTO.DadosCadastroOrcamentoAdmin;
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
    public Orcamento cadastrarOrcamentosPorUsuario (DadosCadastroOrcamentoAdmin dados, Authentication authentication){

        Orcamento orcamento = new Orcamento(dados);
        orcamento.setUsuario(usuarioService.getUsuarioLogado(authentication));

        return repository.save(orcamento);
    }


    @Transactional
    public Orcamento cadastrarOrcamentos (DadosCadastroOrcamentoAdmin dados){
        Orcamento orcamento = new Orcamento(dados);
        orcamento.setUsuario(usuarioRepository.getReferenceById(dados.usuarioId()));
        return repository.save(orcamento);
    }


    public Page<DadosCadastroOrcamentoAdmin> listarOrcamentosPorUsuario(Authentication authentication, Pageable pageable){
        return repository.findByUsuario(usuarioService.getUsuarioLogado(authentication),pageable).map(DadosCadastroOrcamentoAdmin::new);
    }


    public Page<DadosCadastroOrcamentoAdmin> listarOrcamentos(Pageable page){
        return repository.findAll(page).map(DadosCadastroOrcamentoAdmin::new);
    }


    public DadosCadastroOrcamentoAdmin buscarOrcamentoPorId(Long id){
        Orcamento orcamento = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Orçamento não encontrado"));
        return new DadosCadastroOrcamentoAdmin(orcamento);
    }


    public Page<DadosCadastroOrcamentoAdmin> buscarOrcamentoPorNomePorUsuario(Authentication authentication, String nome, Pageable page ){
        return repository.findByUsuarioAndNomeContaining(usuarioService.getUsuarioLogado(authentication),nome,page).map(DadosCadastroOrcamentoAdmin::new);
    }


    public Page<DadosCadastroOrcamentoAdmin> buscarOrcamentoPorNome(String nome, Pageable page){
        return repository.findByNomeContainingIgnoreCase(nome,page).map(DadosCadastroOrcamentoAdmin::new);
    }


    @Transactional
    public DadosCadastroOrcamentoAdmin atualizarOrcamento(DadosAtulizarOrcamento dados){
        Orcamento orcamento = repository.getReferenceById(dados.id());
        orcamento.atualizarInformacoes(dados);
        return new DadosCadastroOrcamentoAdmin(orcamento);
    }


    @Transactional
    public DadosCadastroOrcamentoAdmin atulizarOrcamentoPorUsuario(Authentication authentication, DadosAtulizarOrcamento dados){
        Orcamento orcamento = repository.findByUsuarioAndId(usuarioService.getUsuarioLogado(authentication),dados.id())
                .orElseThrow(EntityNotFoundException::new);
        orcamento.atualizarInformacoes(dados);
        return new DadosCadastroOrcamentoAdmin(orcamento);
    }


    @Transactional
    public void excluirOrcamento(Long id){
        var orcamento = repository.getReferenceById(id);

        orcamento.setAtivo(false);
    }


    @Transactional
    public void excluirOrcamentoPorUsuario (Authentication authentication, Long id){
        Usuario usuario = usuarioService.getUsuarioLogado(authentication);

        Orcamento orcamento = repository.findByUsuarioAndId(usuario,id).orElseThrow(EntityNotFoundException::new);

        orcamento.setAtivo(false);
    }

}
