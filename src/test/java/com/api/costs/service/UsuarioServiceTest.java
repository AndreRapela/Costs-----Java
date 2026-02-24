package com.api.costs.controllers;

import com.api.costs.orcamento.Categoria;
import com.api.costs.orcamento.DTO.DadosAtulizarOrcamento;
import com.api.costs.orcamento.DTO.DadosCadastroOrcamentoAdmin;
import com.api.costs.orcamento.Orcamento;
import com.api.costs.orcamento.Status;
import com.api.costs.service.OrcamentoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrcamentoController.class)
class OrcamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrcamentoService service;

    @Autowired
    private ObjectMapper objectMapper;

    private DadosCadastroOrcamentoAdmin dados;
    private Orcamento orcamento;
    private Page<DadosCadastroOrcamentoAdmin> page;
    private DadosAtulizarOrcamento dadosAtualizados;
    private DadosCadastroOrcamentoAdmin retorno;

    @BeforeEach
    void setup() {
        dados = new DadosCadastroOrcamentoAdmin(
                "energia",
                BigDecimal.valueOf(200),
                Categoria.DEBITO,
                Status.PENDENTE,
                1L
        );

        orcamento = new Orcamento(dados);
        orcamento.setId(1L);

        page = new PageImpl<>(List.of(dados));

        dadosAtualizados = new DadosAtulizarOrcamento(
                1L,
                "água",
                BigDecimal.valueOf(300),
                Status.ABATIDO
        );

        retorno = new DadosCadastroOrcamentoAdmin(
                "água",
                BigDecimal.valueOf(300),
                Categoria.DEBITO,
                Status.ABATIDO,
                1L
        );
    }

    // 1 - POST /costs (usuário)
    @Test
    void cadastrarOrcamentoPorUsuario() throws Exception {
        Authentication auth = mock(Authentication.class);

        when(service.cadastrarOrcamentosPorUsuario(eq(dados), same(auth)))
                .thenReturn(orcamento);

        mockMvc.perform(post("/costs")
                        .principal(auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dados)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("energia"))
                .andExpect(jsonPath("$.usuarioId").value(1L));
    }

    // 2 - POST /costs/admin
    @Test
    void cadastrarOrcamentoAdmin() throws Exception {
        when(service.cadastrarOrcamentos(eq(dados)))
                .thenReturn(orcamento);

        mockMvc.perform(post("/costs/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dados)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("energia"));
    }

    // 3 - GET /costs (usuário)
    @Test
    void listarPorUsuario() throws Exception {
        Authentication auth = mock(Authentication.class);

        when(service.listarOrcamentosPorUsuario(same(auth), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/costs")
                        .principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    // 4 - GET /costs/admin
    @Test
    void listarAdmin() throws Exception {
        when(service.listarOrcamentos(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/costs/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    // 5 - GET /costs/admin/{id}
    @Test
    void buscarPorIdAdmin() throws Exception {
        when(service.buscarOrcamentoPorId(eq(1L)))
                .thenReturn(dados);

        mockMvc.perform(get("/costs/admin/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("energia"));
    }

    // 6 - GET /costs/find?nome=energia (usuário)
    @Test
    void buscarPorNomeUsuario() throws Exception {
        Authentication auth = mock(Authentication.class);

        when(service.buscarOrcamentoPorNomePorUsuario(
                same(auth), eq("energia"), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/costs/find")
                        .param("nome", "energia")
                        .principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("energia"));
    }

    // 7 - GET /costs/admin/find?nome=energia
    @Test
    void buscarPorNomeAdmin() throws Exception {
        when(service.buscarOrcamentoPorNome(eq("energia"), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/costs/admin/find")
                        .param("nome", "energia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    // 8 - PUT /costs/admin
    @Test
    void atualizarAdmin() throws Exception {
        when(service.atualizarOrcamento(eq(dadosAtualizados)))
                .thenReturn(retorno);

        mockMvc.perform(put("/costs/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosAtualizados)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("água"));
    }

    // 9 - PUT /costs (usuário)
    @Test
    void atualizarUsuario() throws Exception {
        Authentication auth = mock(Authentication.class);

        when(service.atulizarOrcamentoPorUsuario(same(auth), eq(dadosAtualizados)))
                .thenReturn(retorno);

        mockMvc.perform(put("/costs")
                        .principal(auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosAtualizados)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ABATIDO"));
    }

    // 10 - DELETE /costs/admin/{id}
    @Test
    void deletarAdmin() throws Exception {
        doNothing().when(service).excluirOrcamento(eq(1L));

        mockMvc.perform(delete("/costs/admin/1"))
                .andExpect(status().isNoContent());
    }

    // 11 - DELETE /costs/{id} (usuário)
    @Test
    void deletarUsuario() throws Exception {
        Authentication auth = mock(Authentication.class);

        doNothing().when(service)
                .excluirOrcamentoPorUsuario(same(auth), eq(1L));

        mockMvc.perform(delete("/costs/1")
                        .principal(auth))
                .andExpect(status().isNoContent());
    }
}
