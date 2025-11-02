package com.api.costs.service;

import com.api.costs.repository.UsuarioRepository;
import com.api.costs.usuario.DTOs.DadosAtualizarUsuario;
import com.api.costs.usuario.DTOs.DadosCadastroUsuario;
import com.api.costs.usuario.DTOs.DadosListarUsuario;
import com.api.costs.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository repository;

    public Usuario getUsuarioLogado (Authentication authentication){
        String login = authentication.getName();
        Usuario usuario = repository.findByLogin(login);

        if(usuario == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!");
        }

        return  usuario;
    }


    @Transactional
    public Usuario cadastrarUsuario(DadosCadastroUsuario dados){
        return repository.save(new Usuario(dados));
    }


    public Page<DadosListarUsuario> listarUsuarios(Pageable page){
        return repository.findAll(page).map(DadosListarUsuario::new);
    }


    public DadosListarUsuario buscarUsuarioPorLogin( String login){
        return new DadosListarUsuario(repository.findByLogin(login));
    }


    public DadosListarUsuario buscarPorId(Long id){
        return new DadosListarUsuario(repository.getReferenceById(id));
    }


    @Transactional
    public DadosCadastroUsuario atualizarSenha(DadosAtualizarUsuario dados){
        Usuario usuario = repository.getReferenceById(dados.id());
        usuario.atualizarSenha(dados);
        return new DadosCadastroUsuario(usuario);
    }


    @Transactional
    public DadosCadastroUsuario atualizarSenhaDoUsuario(Authentication authentication,DadosAtualizarUsuario dados){
        Usuario usuario = getUsuarioLogado(authentication);
        usuario.atualizarSenha(dados);
        repository.save(usuario);
        return  new DadosCadastroUsuario(usuario);
    }


    @Transactional
    public void excluirUsuario(Long id){ repository.deleteById(id); }


    @Transactional
    public void excluirUsuarioSelf(Authentication authentication){
        repository.delete(getUsuarioLogado(authentication));
    }
}
