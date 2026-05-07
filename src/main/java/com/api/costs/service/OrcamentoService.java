package com.api.costs.service;

import com.api.costs.orcamento.DTO.*;
import com.api.costs.orcamento.Orcamento;
import com.api.costs.repository.OrcamentoRepository;
import com.api.costs.repository.UsuarioRepository;
import com.api.costs.usuario.Usuario;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@CacheConfig(cacheNames ="orcamento")
public class OrcamentoService {

    @Autowired
    private OrcamentoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @CacheEvict(allEntries = true)
    @Transactional
    public Orcamento cadastrarOrcamentosPorUsuario (DadosCadastroOrcamento dados, Authentication authentication){

        Orcamento orcamento = new Orcamento(dados);
        orcamento.setUsuario(usuarioService.getUsuarioLogado(authentication));

        return repository.save(orcamento);
    }

    @CacheEvict(allEntries = true)
    @Transactional
    public Orcamento cadastrarOrcamentos (DadosCadastroOrcamentoAdmin dados){
        Orcamento orcamento = new Orcamento(dados);
        orcamento.setUsuario(usuarioRepository.getReferenceById(dados.usuarioId()));
        return repository.save(orcamento);
    }

    @Cacheable(key = "'listByUser:' + '#authentication.name + ':' + #page.pageNumber + ':' + #page.pageSize")
    public Page<DadosListarOrcamento> listarOrcamentosPorUsuario(Authentication authentication, Pageable pageable){
        return repository.findByUsuario(usuarioService.getUsuarioLogado(authentication),pageable).map(DadosListarOrcamento::new);
    }

    @Cacheable(key = "'list:' + #page.pageNumber + ':' + #page.pageSize")
    public Page<DadosListarOrcamentoAdmin> listarOrcamentos(Pageable page){
        return repository.findAll(page).map(DadosListarOrcamentoAdmin::new);
    }

    @Cacheable(key = "'byId:' + #id")
    public DadosListarOrcamentoAdmin buscarOrcamentoPorId(Long id){
        Orcamento orcamento = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Orçamento não encontrado"));
        return new DadosListarOrcamentoAdmin(orcamento);
    }

    @Cacheable(key = "'byUserAndByName:' + authentication.name + ':' + #nome + #page.pageNumber + ':' + #page.pageSize + ':' + #page.sorte.toString()")
    public Page<DadosListarOrcamento> buscarOrcamentoPorNomePorUsuario(Authentication authentication, String nome, Pageable page ){
        return repository.findByUsuarioAndNomeContainingIgnoreCase(usuarioService.getUsuarioLogado(authentication),nome,page).map(DadosListarOrcamento::new);
    }

    @Cacheable(key = "'byName:' + #nome +':' + ':' + #page.pageNumber + ':' + #page.pageSize + ':' + #page.sort.toString()")
    public Page<DadosListarOrcamentoAdmin> buscarOrcamentoPorNome(String nome, Pageable page){
        return repository.findByNomeContainingIgnoreCase(nome,page).map(DadosListarOrcamentoAdmin::new);
    }

    @CacheEvict(allEntries = true)
    @Transactional
    public DadosListarOrcamentoAdmin atualizarOrcamento(DadosAtulizarOrcamento dados){
        Orcamento orcamento = repository.getReferenceById(dados.id());
        orcamento.atualizarInformacoes(dados);
        return new DadosListarOrcamentoAdmin(orcamento);
    }

    @CacheEvict(allEntries = true)
    @Transactional
    public DadosListarOrcamento atulizarOrcamentoPorUsuario(Authentication authentication, DadosAtulizarOrcamento dados){
        Orcamento orcamento = repository.findByUsuarioAndId(usuarioService.getUsuarioLogado(authentication),dados.id())
                .orElseThrow(EntityNotFoundException::new);
        orcamento.atualizarInformacoes(dados);
        return new DadosListarOrcamento(orcamento);
    }

    @CacheEvict(allEntries = true)
    @Transactional
    public void excluirOrcamento(Long id){
        var orcamento = repository.getReferenceById(id);

        orcamento.setAtivo(false);
    }

    @CacheEvict(allEntries = true)
    @Transactional
    public void excluirOrcamentoPorUsuario (Authentication authentication, Long id){
        Usuario usuario = usuarioService.getUsuarioLogado(authentication);

        Orcamento orcamento = repository.findByUsuarioAndId(usuario,id).orElseThrow(EntityNotFoundException::new);

        orcamento.setAtivo(false);
    }

}
