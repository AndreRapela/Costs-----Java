package com.api.costs.controllers;

import com.api.costs.orcamento.Categoria;
import com.api.costs.orcamento.DTO.DadosAtulizarOrcamento;
import com.api.costs.orcamento.DTO.DadosCadastroOrcamento;
import com.api.costs.orcamento.Orcamento;
import com.api.costs.orcamento.Status;
import com.api.costs.repository.OrcamentoRepository;
import com.api.costs.service.OrcamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


class OrcamentoControllerTest {

    @Mock
    private OrcamentoService service;

    @Mock
    private Authentication authentication;

    @InjectMocks
    OrcamentoController controller;

    private DadosCadastroOrcamento dados;
    private  Orcamento orcamento;
    private Page<DadosCadastroOrcamento> page;
    private DadosAtulizarOrcamento dadosAtualizados;
    private DadosCadastroOrcamento retorno;

    @BeforeEach
    void CriarDTO(){
        MockitoAnnotations.openMocks(this);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        dados = new DadosCadastroOrcamento
                ( 1L,"energia", BigDecimal.valueOf(200), Categoria.DEBITO, Status.PENDENTE,1L);
        orcamento = new Orcamento(dados);
        orcamento.setId(1L);
        page = new PageImpl<>(List.of(dados));
        dadosAtualizados = new DadosAtulizarOrcamento(1L, "água", BigDecimal.valueOf(300), Status.ABATIDO);
        retorno = new DadosCadastroOrcamento
                (1L, "água", BigDecimal.valueOf(300),Categoria.DEBITO, Status.ABATIDO,1L);

    }


    @Test
    @DisplayName("Deve retornar o status 201 quando cadastrar um orçamento do usuário logado")
    void cadastrarOrcamentoPorUsuario(){
        when(service.cadastrarOrcamentosPorUsuario(any(DadosCadastroOrcamento.class),any(Authentication.class)))
                .thenReturn(orcamento);

        ResponseEntity<Orcamento> response = controller.cadastrarOrcamentosPorUsuario(dados,authentication);

        assertAll("validação dos campos do orçamento retornado",
                () -> assertEquals(1L, response.getBody().getId()),
                () -> assertEquals("energia", response.getBody().getNome()),
                () -> assertEquals(BigDecimal.valueOf(200), response.getBody().getValor()),
                () -> assertEquals(Categoria.DEBITO, response.getBody().getCategoria()),
                () -> assertEquals(Status.PENDENTE, response.getBody().getStatus()));
    }


    @Test
    @DisplayName("Deve retornar o status 201 created quando cadastrar um orçamento com sucesso")
    void cadastrarOrcamentos() {
        when(service.cadastrarOrcamentos(any(DadosCadastroOrcamento.class))).thenReturn(orcamento);

        ResponseEntity<Orcamento> response = controller.cadastrarOrcamentos(dados);

        assertAll("validação dos campos do orçamento retornado",
                () -> assertEquals(1L, response.getBody().getId()),
                () -> assertEquals("energia", response.getBody().getNome()),
                () -> assertEquals(BigDecimal.valueOf(200), response.getBody().getValor()),
                () -> assertEquals(Categoria.DEBITO, response.getBody().getCategoria()),
                () -> assertEquals(Status.PENDENTE, response.getBody().getStatus()));
    }



    @Test
    @DisplayName("Deve listar os orçamentos do usuário logado (200 OK)")
    void listarOrcamentosPorUsuario(){
                when(service.listarOrcamentosPorUsuario(any(Authentication.class),any(Pageable.class))).thenReturn(page);

        ResponseEntity <Page<DadosCadastroOrcamento>> response = controller.listarOrcamentoPorUsuario(authentication,Pageable.unpaged());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page.getTotalElements(), response.getBody().getContent().size());
    }


    @Test
    @DisplayName("Deve listar todos os orçamentos para o adm com sucesso (200 OK)")
    void listarOrcamento() {
        when(service.listarOrcamentos(any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<DadosCadastroOrcamento>> response = controller.listarOrcamentos(Pageable.unpaged());

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(page.getTotalElements(), response.getBody().getTotalElements());
    }


    @Test
    @DisplayName("Deve retornar a lista de orçamentos paginados com sucesso!")
    void BuscarOrcamentoPorId() {
        when(service.buscarOrcamentoPorId(anyLong())).thenReturn(dados);

        ResponseEntity<DadosCadastroOrcamento> response = controller.buscarOrcamentoPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    @DisplayName("Deve retornar os orçamentos buscado por nome")
    void buscarOrcamentoPorNome(){
        when(service.buscarOrcamentoPorNome(anyString(),any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<DadosCadastroOrcamento>> response = controller.buscarOrcamentoPorNome("energia",Pageable.unpaged());

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(page.getTotalElements(),response.getBody().getTotalElements());
    }


    @Test
    @DisplayName("Deve retornar os orçamentos buscados por nome do próprio usuário")
    void buscarOrcamentoPorNomePorUsuario(){
        when(service.buscarOrcamentoPorNomePorUsuario(any(Authentication.class),anyString(),any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<DadosCadastroOrcamento>> response =
                controller.buscarOrcamentoPorUsuarioPorNome(authentication,"energia",Pageable.unpaged());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(page.getTotalElements(), response.getBody().getTotalElements());
    }


    @Test
    @DisplayName("Deve retornar os valores atualizados do orçamento passado")
    void atualizarOrcamento() {
        when(service.atualizarOrcamento(any(DadosAtulizarOrcamento.class))).thenReturn(retorno);

        ResponseEntity<DadosCadastroOrcamento> response = controller.atualizarOrcamento(dadosAtualizados);

        assertAll("Validação dos campos atualizados",
                () ->assertNotNull(response.getBody()),
                () -> assertEquals(retorno.nome(), response.getBody().nome()),
                () -> assertEquals(retorno.valor(), response.getBody().valor()),
                () -> assertEquals(retorno.status(), response.getBody().status()));
    }


    @Test
    @DisplayName("Deve retornar os valores atualizados do orçamento passado do proprio usuário")
    void atualizarOrcamentoPorUsuario(){
        when(service.atulizarOrcamentoPorUsuario(any(Authentication.class),any(DadosAtulizarOrcamento.class))).thenReturn(retorno);

        ResponseEntity<DadosCadastroOrcamento> response = controller.atualizarOrcamentoPorUsuario(authentication,dadosAtualizados);

        assertAll("Validação dos campos atualizados",
                () ->assertNotNull(response.getBody()),
                () -> assertEquals(retorno.nome(), response.getBody().nome()),
                () -> assertEquals(retorno.valor(), response.getBody().valor()),
                () -> assertEquals(retorno.status(), response.getBody().status()));
    }


    @Test
    @DisplayName("exclui um orçamento")
    void excluirOrcamento() {
        doNothing().when(service).excluirOrcamento(anyLong());

        ResponseEntity<Void> response = controller.excluirOrcamento(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }


    @Test
    @DisplayName("Exclui um orçamento do usuário")
    void excluirOrcamentoPorUsuario() {
        doNothing().when(service).excluirOrcamentoPorUsuario(any(Authentication.class),anyLong());

        ResponseEntity<Void> response = controller.excluirOrcamentoPorUsuario(authentication,1L);

        assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());
        assertNull(response.getBody());
    }


}