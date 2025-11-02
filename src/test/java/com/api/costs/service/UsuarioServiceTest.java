package com.api.costs.service;

import com.api.costs.repository.UsuarioRepository;
import com.api.costs.usuario.DTOs.DadosAtualizarUsuario;
import com.api.costs.usuario.DTOs.DadosCadastroUsuario;
import com.api.costs.usuario.DTOs.DadosListarUsuario;
import com.api.costs.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private Authentication authentication;

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    private Usuario usuario;
    private Page<Usuario> page;
    private DadosCadastroUsuario dadosCadastro;
    private DadosAtualizarUsuario dadosAtualizar;

        @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setLogin("usuarioTeste");
        usuario.setSenha("senhaTeste");
        dadosCadastro = new DadosCadastroUsuario(usuario);
        dadosAtualizar = new DadosAtualizarUsuario(1L,"senhaAtualizada");
        page = new PageImpl<>(List.of(usuario));

    }

    @Test
    void getUsuarioLogado() {
        when(authentication.getName()).thenReturn("usuarioTeste");
        when(repository.findByLogin(anyString())).thenReturn(usuario);

        Usuario resultado = service.getUsuarioLogado(authentication);

        assertEquals(usuario.getId(),resultado.getId());
        verify(repository,times(1)).findByLogin(anyString());
    }

    @Test
    void cadastrarUsuario() {
         when(repository.save(any(Usuario.class))).thenReturn(usuario);

         Usuario resultado = service.cadastrarUsuario(dadosCadastro);

         assertNotNull(resultado);
         verify(repository,times(1)).save(any(Usuario.class));
    }

    @Test
    void listarUsuarios() {
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<DadosListarUsuario> resultado = service.listarUsuarios(Pageable.unpaged());

        assertEquals(page.getTotalElements(),resultado.getTotalElements());
        verify(repository,times(1)).findAll(any(Pageable.class));
    }

    @Test
    void buscarUsuarioPorLogin() {
         when(repository.findByLogin(anyString())).thenReturn(usuario);

         DadosListarUsuario resultado = service.buscarUsuarioPorLogin("usuarioTeste");

         assertNotNull(resultado);
         verify(repository,times(1)).findByLogin(anyString());
    }

    @Test
    void buscarPorId() {
         when(repository.getReferenceById(anyLong())).thenReturn(usuario);

         DadosListarUsuario resultado = service.buscarPorId(1L);

         assertNotNull(resultado);
         verify(repository,times(1)).getReferenceById(anyLong());
    }

    @Test
    void atualizarSenha() {
         when(repository.getReferenceById(anyLong())).thenReturn(usuario);

         DadosCadastroUsuario resultado = service.atualizarSenha(dadosAtualizar);

         assertNotNull(resultado);
         verify(repository,times(1)).getReferenceById(anyLong());
    }

    @Test
    void atualizarSenhaDoUsuario() {
         when(authentication.getName()).thenReturn("usuarioTeste");
         when(repository.findByLogin(anyString())).thenReturn(usuario);
         when(repository.save(any(Usuario.class))).thenReturn(usuario);

         DadosCadastroUsuario resultado = service.atualizarSenhaDoUsuario(authentication,dadosAtualizar);

         assertNotNull(resultado);
         verify(repository,times(1)).findByLogin(anyString());
         verify(repository,times(1)).save(any(Usuario.class));
    }

    @Test
    void excluirUsuario() {
         doNothing().when(repository).deleteById(anyLong());

         service.excluirUsuario(1L);

         verify(repository,times(1)).deleteById(anyLong());
    }

    @Test
    void excluirUsuarioSelf() {
         when(authentication.getName()).thenReturn("usuarioTeste");
         when(repository.findByLogin("usuarioTeste")).thenReturn(usuario);
         doNothing().when(repository).delete(any(Usuario.class));

         service.excluirUsuarioSelf(authentication);

         verify(repository,times(1)).delete(any(Usuario.class));
         verify(repository,times(1)).findByLogin(anyString());
    }
}