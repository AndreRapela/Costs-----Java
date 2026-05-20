package com.api.costs.controllers;



import com.api.costs.infra.SecurityFilter;
import com.api.costs.service.UsuarioService;
import com.api.costs.usuario.DTOs.DadosAtualizarUsuario;
import com.api.costs.usuario.DTOs.DadosCadastroUsuario;
import com.api.costs.usuario.DTOs.DadosListarUsuario;
import com.api.costs.usuario.Usuario;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void setUp() {
        dadosCadastro = new DadosCadastroUsuario("usuarioTeste","senhaTeste");
        usuario = new Usuario(dadosCadastro);
        usuario.setId(1L);
        dados = new DadosListarUsuario(usuario);
        dadosAtualizar = new DadosAtualizarUsuario(1L,"senhaAtualizada");
        page = new PageImpl<>(List.of(dados));
    }

    @Test
    void cadastrarUsuario( ) throws Exception {
        when(service.cadastrarUsuario(any(DadosCadastroUsuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosCadastro)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.login").value("usuarioTeste"));

    }


    @Test
    void listarUsuarios() throws Exception {
        when(service.listarUsuarios(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/registro/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].login").value("usuarioTeste"));
    }

    @Test
    void buscarUsuarioPorLogin() throws Exception{
        when(service.buscarUsuarioPorLogin(anyString(),any(Pageable.class))).thenReturn(page);

       mockMvc.perform(get("/registro/admin/find")
                       .param("login","usuarioTeste"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content[0].login").value("usuarioTeste"));
    }

    @Test
    void buscarUsuarioPorId() throws Exception{
        when(service.buscarPorId(anyLong())).thenReturn(dados);

       mockMvc.perform(get("/registro/admin/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.login").value("usuarioTeste"));
    }

    @Test
    void alterarSenha() throws Exception{
        usuario.setSenha(dadosAtualizar.senha());

        when(service.atualizarSenha(any(DadosAtualizarUsuario.class))).thenReturn(dadosCadastro);

       mockMvc.perform(put("/registro/admin")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(dadosAtualizar)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.senha").value("senhaTeste"));
    }

//    @Test
//    void alterarSenhaDoUsuario() {
//
//        when(service.atualizarSenhaDoUsuario(any(Authentication.class),any(DadosAtualizarUsuario.class))).thenReturn(dadosCadastro);
//
//        ResponseEntity<DadosCadastroUsuario> response = controller.alterarSenhaDoUsuario(authentication,dadosAtualizar);
//
//        assertAll("Validação da nova senha do próprio usuário",
//                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
//                () -> assertNotNull(response.getBody()),
//                () -> assertEquals("senhaAtualizada", response.getBody().senha()));
//    }

    @Test
    void excluirUsuario() throws Exception{
        doNothing().when(service).excluirUsuario(anyLong());

        mockMvc.perform(delete("/registro/admin/1"))
                .andExpect(status().isNoContent());

    }

//    @Test
//    void excluirUsuarioSelf() {
//        doNothing().when(service).excluirUsuarioSelf(any(Authentication.class));
//
//        ResponseEntity<Void> response = controller.excluirUsuarioSelf(authentication);
//
//        assertAll("Validação do usuário excluído",
//                () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()),
//                () -> assertNull(response.getBody()));
//    }
}