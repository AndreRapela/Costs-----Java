package com.api.costs.service;

import com.api.costs.itemOrcamento.DTO.*;
import com.api.costs.itemOrcamento.ItemOrcamento;
import com.api.costs.repository.ItemOrcamentoRepository;
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
@CacheConfig(cacheNames ="item_orcamento")
public class ItemOrcamentoService {

    @Autowired
    private ItemOrcamentoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @CacheEvict(allEntries = true)
    @Transactional
    public ItemOrcamento cadastrarItemOrcamentosPorUsuario (DadosCadastroItemOrcamento dados, Authentication authentication){

        ItemOrcamento itemOrcamento = new ItemOrcamento(dados);
        itemOrcamento.setUsuario(usuarioService.getUsuarioLogado(authentication));

        return repository.save(itemOrcamento);
    }

    @CacheEvict(allEntries = true)
    @Transactional
    public ItemOrcamento cadastrarItemOrcamentos (DadosCadastroItemOrcamentoAdmin dados){
        ItemOrcamento itemOrcamento = new ItemOrcamento(dados);
        itemOrcamento.setUsuario(usuarioRepository.getReferenceById(dados.usuarioId()));
        return repository.save(itemOrcamento);
    }

    @Cacheable(key = "'listByUser:' + '#authentication.name + ':' + #page.pageNumber + ':' + #page.pageSize")
    public Page<DadosListarItemOrcamento> listarItemOrcamentosPorUsuario(Authentication authentication, Pageable pageable){
        return repository.findByUsuario(usuarioService.getUsuarioLogado(authentication),pageable).map(DadosListarItemOrcamento::new);
    }

    @Cacheable(key = "'list:' + #page.pageNumber + ':' + #page.pageSize")
    public Page<DadosListarItemOrcamentoAdmin> listarItemOrcamentos(Pageable page){
        return repository.findAll(page).map(DadosListarItemOrcamentoAdmin::new);
    }

    @Cacheable(key = "'byId:' + #id")
    public DadosListarItemOrcamentoAdmin buscarItemOrcamentoPorId(Long id){
        ItemOrcamento itemOrcamento = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Orçamento não encontrado"));
        return new DadosListarItemOrcamentoAdmin(itemOrcamento);
    }

    @Cacheable(key = "'byUserAndByName:' + authentication.name + ':' + #nome + #page.pageNumber + ':' + #page.pageSize + ':' + #page.sorte.toString()")
    public Page<DadosListarItemOrcamento> buscarItemOrcamentoPorNomePorUsuario(Authentication authentication, String nome, Pageable page ){
        return repository.findByUsuarioAndNomeContainingIgnoreCase(usuarioService.getUsuarioLogado(authentication),nome,page).map(DadosListarItemOrcamento::new);
    }

    @Cacheable(key = "'byName:' + #nome +':' + ':' + #page.pageNumber + ':' + #page.pageSize + ':' + #page.sort.toString()")
    public Page<DadosListarItemOrcamentoAdmin> buscarItemOrcamentoPorNome(String nome, Pageable page){
        return repository.findByNomeContainingIgnoreCase(nome,page).map(DadosListarItemOrcamentoAdmin::new);
    }

    @CacheEvict(allEntries = true)
    @Transactional
    public DadosListarItemOrcamentoAdmin atualizarItemOrcamento(DadosAtulizarItemOrcamento dados){
        ItemOrcamento itemOrcamento = repository.getReferenceById(dados.id());
        itemOrcamento.atualizarInformacoes(dados);
        return new DadosListarItemOrcamentoAdmin(itemOrcamento);
    }

    @CacheEvict(allEntries = true)
    @Transactional
    public DadosListarItemOrcamento atulizarItemOrcamentoPorUsuario(Authentication authentication, DadosAtulizarItemOrcamento dados){
        ItemOrcamento itemOrcamento = repository.findByUsuarioAndId(usuarioService.getUsuarioLogado(authentication),dados.id())
                .orElseThrow(EntityNotFoundException::new);
        itemOrcamento.atualizarInformacoes(dados);
        return new DadosListarItemOrcamento(itemOrcamento);
    }

    @CacheEvict(allEntries = true)
    @Transactional
    public void excluirItemOrcamento(Long id){
        var orcamento = repository.getReferenceById(id);

        orcamento.setAtivo(false);
    }

    @CacheEvict(allEntries = true)
    @Transactional
    public void excluirItemOrcamentoPorUsuario (Authentication authentication, Long id){
        Usuario usuario = usuarioService.getUsuarioLogado(authentication);

        ItemOrcamento itemOrcamento = repository.findByUsuarioAndId(usuario,id).orElseThrow(EntityNotFoundException::new);

        itemOrcamento.setAtivo(false);
    }

}
