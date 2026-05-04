package com.api.costs.controllers;



import com.api.costs.infra.SecurityFilter;
import com.api.costs.service.UsuarioService;
import com.api.costs.usuario.DTOs.DadosAtualizarUsuario;
import com.api.costs.usuario.DTOs.DadosCadastroUsuario;
import com.api.costs.usuario.DTOs.DadosListarUsuario;
import com.api.costs.usuario.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = UsuarioController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService service;

    @MockBean
    private SecurityFilter securityFilter;

    @MockBean
    private JpaMetamodelMappingContext jpaMappingContext;

    private DadosCadastroUsuario dadosCadastro;
    private DadosAtualizarUsuario dadosAtualizar;
    private Usuario usuario;
    private DadosListarUsuario dados;
    private Page<DadosListarUsuario> page;

    @BeforeEach
    void CriarDTO() {
        dadosCadastro = new DadosCadastroUsuario("usuarioTeste","senhaTeste");
        usuario = new Usuario(dadosCadastro);
        usuario.setId(1L);
        dadosAtualizar = new DadosAtualizarUsuario(1L,"senhaAtualizada");
        page = new PageImpl<>(List.of(dados));
    }

    @Test
    void cadastrarUsuario( ) {
        when(service.cadastrarUsuario(any(DadosCadastroUsuario.class))).thenReturn(usuario);

        ResponseEntity<Usuario> response = controller.cadastrarUsuario(dadosCadastro);

        assertAll("validação dos campos do usuário retornado",
                () -> assertEquals(1L, response.getBody().getId()),
                () -> assertEquals("usuarioTeste", response.getBody().getLogin()),
                () -> assertEquals("senhaTeste" , response.getBody().getSenha()));
    }

    @Test
    void listarUsuarios() {
        when(service.listarUsuarios(any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<DadosListarUsuario>> response = controller.listarUsuarios(Pageable.unpaged());

        assertAll("Validação dos campos da página retornada",
                () ->assertEquals(HttpStatus.OK, response.getStatusCode()),
                () ->assertEquals(page.getTotalElements(), response.getBody().getTotalElements()),
                () -> assertNotNull(response.getBody()));
    }

    @Test
    void buscarUsuarioPorLogin() {
        when(service.buscarUsuarioPorLogin(anyString(),any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<DadosListarUsuario>> response = controller.buscarUsuarioPorLogin("usuarioTeste",Pageable.unpaged());

        assertAll("validação dos campos do usuário retornado",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("usuarioTeste", response.getBody().getContent().get(0).login()));
    }

    @Test
    void buscarUsuarioPorId() {
        when(service.buscarPorId(anyLong())).thenReturn(dados);

        ResponseEntity<DadosListarUsuario> response = controller.buscarUsuarioPorId(1L);

        assertAll("validação dos campos do usuário retornado",
                () -> assertEquals(HttpStatus.OK,response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("usuarioTeste", response.getBody().login()));
    }

    @Test
    void alterarSenha() {
        when(service.atualizarSenha(any(DadosAtualizarUsuario.class))).thenReturn(retornoDadosAtualizado);

        ResponseEntity<DadosCadastroUsuario> response = controller.alterarSenha(dadosAtualizar);

        assertAll("Validação da nova senha atualizada",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals("senhaAtualizada", response.getBody().senha()),
                () -> assertNotNull(response.getBody()));
    }

    @Test
    void alterarSenhaDoUsuario() {
        when(service.atualizarSenhaDoUsuario(any(Authentication.class),any(DadosAtualizarUsuario.class))).thenReturn(retornoDadosAtualizado);

        ResponseEntity<DadosCadastroUsuario> response = controller.alterarSenhaDoUsuario(authentication,dadosAtualizar);

        assertAll("Validação da nova senha do próprio usuário",
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("senhaAtualizada", response.getBody().senha()));
    }

    @Test
    void excluirUsuario() {
        doNothing().when(service).excluirUsuario(anyLong());

        ResponseEntity<Void> response = controller.excluirUsuario(1L);

        assertAll("Validação do usuário excluído",
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()),
                () -> assertNull(response.getBody()));
    }

    @Test
    void excluirUsuarioSelf() {
        doNothing().when(service).excluirUsuarioSelf(any(Authentication.class));

        ResponseEntity<Void> response = controller.excluirUsuarioSelf(authentication);

        assertAll("Validação do usuário excluído",
                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()),
                () -> assertNull(response.getBody()));
    }
}