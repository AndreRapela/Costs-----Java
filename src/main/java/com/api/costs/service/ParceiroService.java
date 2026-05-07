package com.api.costs.service;

import com.api.costs.parceiro.Parceiro;
import com.api.costs.parceiro.DTO.*;
import com.api.costs.repository.ParceiroRepository;
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
@CacheConfig(cacheNames = "parceiro")
public class ParceiroService {

    @Autowired
    private ParceiroRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;





    @CacheEvict(allEntries = true)
    @Transactional
    public Parceiro cadastrarParceiro (DadosCadastroParceiroAdmin dados){
        Parceiro parceiro = new Parceiro(dados);
        parceiro.setUsuario(usuarioRepository.getReferenceById(dados.usuarioId()));
        return repository.save(parceiro);
    }


    @CacheEvict(allEntries = true)
    @Transactional
    public Parceiro cadastrarParceiroPorUsuario (DadosCadastroParceiro dados , Authentication authentication){
        Parceiro parceiro = new Parceiro(dados);
        parceiro.setUsuario(usuarioService.getUsuarioLogado(authentication));
        return repository.save(parceiro);
    }

    @Cacheable("'listarParceiros:' + #page.pageNumber + ':' + #page.pageSize")
    public Page<DadosListarParceiroAdmin> listarParceiro (Pageable page){
        return repository.findAll(page).map(DadosListarParceiroAdmin::new);
    }


    @Cacheable("'ListarDeParceirosDoUsuario:' + #authentication.name + ':' + #page.pageNumber + ':' + #page.pageSize")
    public Page<DadosListarParceiro> listarParceiroPorUsuario(Pageable page, Authentication authentication){
        Usuario usuario = usuarioService.getUsuarioLogado(authentication);
        return repository.findByUsuario(usuario,page).map(DadosListarParceiro::new);
    }

    @Cacheable("'ParceiroPorId:' + #id")
    public DadosListarParceiroAdmin buscarParceiroPorId(Long id){
        Parceiro parceiro = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Parceiro não encontrado"));
        return new DadosListarParceiroAdmin(parceiro);
    }

    @Cacheable("'BuscarParceirosPorNome:' + #nome + ':' + page.pageNumber + ':' + #page.pageSize")
    public  Page<DadosListarParceiroAdmin> buscarParceirosPorNome(String nome, Pageable page){
        return repository.fingByNomeContainingIgnoreCase(nome,page).map(DadosListarParceiroAdmin::new);
    }

    @Cacheable("'BuscarParceiroPorNomePorUsuario:' + #nome + ':' + #authentication.nome + ':' #page.pageNumber + ':' + #page.pageSize ")
    public  Page<DadosListarParceiro> buscarParceiroPorNomePorUsuario(String nome, Pageable page , Authentication authentication){
        Usuario usuario = usuarioService.getUsuarioLogado(authentication);
        return repository.findByNomeAndUsuarioContaingIgnoreCase(nome,page,usuario).map(DadosListarParceiro::new);
    }

    @CacheEvict(allEntries = true)
    public DadosListarParceiroAdmin atualizarParceiro (DadosAtualizarParceiro dados){
        Parceiro parceiro = repository.getReferenceById(dados.id());
        parceiro.atualizarInformacoes(dados);
        return new DadosListarParceiroAdmin(parceiro);
    }

    @CacheEvict(allEntries = true)
    public DadosListarParceiro atualizarParceiroPorUsuario(DadosAtualizarParceiro dados , Authentication authentication){
       Parceiro parceiro = repository.findByUsuarioAndId(usuarioService.getUsuarioLogado(authentication),dados.id())
               .orElseThrow(EntityNotFoundException::new);
       parceiro.atualizarInformacoes(dados);
       return new DadosListarParceiro(parceiro);
    }

    @CacheEvict(allEntries = true)
    public void excluirParceiro(Long id){
        Parceiro parceiro = repository.getReferenceById(id);
        parceiro.excluir();
    }

    @CacheEvict(allEntries = true)
    public void excluirParceiroPorUsuario(Authentication authentication , Long id){
        Usuario usuario = usuarioService.getUsuarioLogado(authentication);
        Parceiro parceiro = repository.findByUsuarioAndId(usuario,id).orElseThrow(EntityNotFoundException::new);
        parceiro.excluir();
    }

}
