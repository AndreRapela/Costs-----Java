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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrcamentoService {

    @Autowired
    private OrcamentoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    private Usuario getUsuarioLogado (Authentication authentication){
        String login = authentication.getName();
        Usuario usuario = usuarioRepository.findByLogin(login);

        if(usuario == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!");
        }

        return  usuario;
    }


    @Transactional
    public Orcamento cadastrarOrcamentosPorUsuario (DadosCadastroOrcamento dados, Authentication authentication){

        Orcamento orcamento = new Orcamento(dados);
        orcamento.setUsuario(getUsuarioLogado(authentication));

        return repository.save(orcamento);
    }


    @Transactional
    public Orcamento cadastrarOrcamentos (DadosCadastroOrcamento dados){
        return repository.save(new Orcamento(dados));
    }


    public Page<DadosCadastroOrcamento> listarOrçamentosPorUsuario(Authentication authentication, Pageable pageable){
        return repository.findByUsuario(getUsuarioLogado(authentication),pageable).map(DadosCadastroOrcamento::new);
    }


    public Page<DadosCadastroOrcamento> listarOrcamentos(Pageable page){
        return repository.findAll(page).map(DadosCadastroOrcamento::new);
    }


    public DadosCadastroOrcamento buscarOrcamentoPorId(Long id){
        return new DadosCadastroOrcamento(repository.getReferenceById(id));
    }


    public Page<DadosCadastroOrcamento> buscarOrcamentoPorNomePorUsuario(Authentication authentication, String nome, Pageable page ){
        return repository.findByUsuarioByNome(getUsuarioLogado(authentication),nome,page).map(DadosCadastroOrcamento::new);
    }


    public Page<DadosCadastroOrcamento> buscarOrcamentoPorNome(String nome, Pageable page){
        return repository.findByNome(nome,page).map(DadosCadastroOrcamento::new);
    }


    @Transactional
    public void excluirOrcamento(Long id){
        repository.deleteById(id);
    }

    @Transactional
    public DadosCadastroOrcamento atualizarOrcamento(DadosAtulizarOrcamento dados){
        Orcamento orcamento = repository.getReferenceById(dados.id());
        orcamento.atualizarInformacoes(dados);
        return new DadosCadastroOrcamento(orcamento);
    }


    @Transactional
    public DadosCadastroOrcamento atulizarOrcamentoPorUsuario(Authentication authentication, DadosAtulizarOrcamento dados){
        Orcamento orcamento = repository.findByUsuarioAndId(getUsuarioLogado(authentication),dados.id())
                .orElseThrow(EntityNotFoundException::new);
        orcamento.atualizarInformacoes(dados);
        return new DadosCadastroOrcamento(orcamento);
    }


    @Transactional
    public void excluirOrcamentoPorUsuario (Long id,Authentication authentication){
        Usuario usuario = getUsuarioLogado(authentication);

        Orcamento orcamento = repository.findByUsuarioAndId(usuario,id).orElseThrow(EntityNotFoundException::new);

        repository.delete(orcamento);
    }

}
