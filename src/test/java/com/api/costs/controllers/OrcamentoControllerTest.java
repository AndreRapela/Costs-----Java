package com.api.costs.controllers;

import com.api.costs.orcamento.Categoria;
import com.api.costs.orcamento.DTO.DadosAtulizarOrcamento;
import com.api.costs.orcamento.DTO.DadosCadastroOrcamento;
import com.api.costs.orcamento.Orcamento;
import com.api.costs.orcamento.Status;
import com.api.costs.repository.OrcamentoRepository;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class OrcamentoControllerTest {

    @Mock
    private OrcamentoRepository repository;

    private DadosCadastroOrcamento dados;

    @InjectMocks
    OrcamentoController controller;


    @BeforeEach
    void CriarDTO(){
        MockitoAnnotations.openMocks(this);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        dados = new DadosCadastroOrcamento
                ( 1L,"energia", BigDecimal.valueOf(200), Categoria.DEBITO, Status.PENDENTE);
    }

    private Orcamento mockOrcamentoPersistido(){
        Orcamento orcamentoPersistido = new Orcamento(dados);

        when(repository.save(any(Orcamento.class))).thenReturn(orcamentoPersistido);
        when(repository.getReferenceById(dados.id())).thenReturn(orcamentoPersistido);

            return orcamentoPersistido;
    }

    private Page<Orcamento> mockPaginacao () {
        Pageable pageable = Pageable.unpaged();
        Orcamento orcamento = mockOrcamentoPersistido();
        Page<Orcamento> page = new PageImpl<>(List.of(orcamento));
        when(repository.findAll(pageable)).thenReturn(page);
        return page;
    }


    @Test
    @DisplayName("Deve retornar o status 201 created quando cadastrar com sucesso")
    void cadastrarComSucesso() {

        mockOrcamentoPersistido();

        ResponseEntity<Orcamento> response = controller.cadastrar(dados);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve retornar o orçamento cadastrado com os valores corretos")
    void cadastrarValidadeCampos(){

        mockOrcamentoPersistido().setId(1L);

        ResponseEntity<Orcamento> response = controller.cadastrar(dados);

        Orcamento orcamento = response.getBody();

        assertNotNull(orcamento.getId());
        assertEquals("energia",orcamento.getNome());
        assertEquals(BigDecimal.valueOf(200),orcamento.getValor());
        assertEquals(Categoria.DEBITO,orcamento.getCategoria());
        assertEquals(Status.PENDENTE, orcamento.getStatus());
    }

    @Test
    @DisplayName("Deve retornar um orçamento buscado por id")
    void listarOrcamento() {
        Page<Orcamento> page = mockPaginacao();

        ResponseEntity<Page<DadosCadastroOrcamento>> response = controller.listarOrcamento(Pageable.unpaged());

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(page.getTotalElements(), response.getBody().getTotalElements());
    }

    @Test
    @DisplayName("Deve retornar a lista de orçamentos paginados com sucesso!")
    void orcamentoPorId() {

        mockOrcamentoPersistido();

        ResponseEntity<DadosCadastroOrcamento> response = controller.orcamentoPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Deve retornar um orçamento buscado por nome")
    void orcamentoPorNome(){
        Pageable pageable = Pageable.unpaged();
        Page page = mockPaginacao();

        when(repository.findByNome(dados.nome(),pageable)).thenReturn(page);

        ResponseEntity<Page<DadosCadastroOrcamento>> response = controller.orcamentoPorNome(dados.nome(),pageable);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(page.getTotalElements(),response.getBody().getTotalElements());
    }


    @Test
    @DisplayName("Deve retornar os valores atualizados do orçamento passado")
    void atualizarOrcamento() {
        mockOrcamentoPersistido();

        DadosAtulizarOrcamento dadosAtualizados = new DadosAtulizarOrcamento
                (1L, "água", BigDecimal.valueOf(300), Status.ABATIDO);

        ResponseEntity<DadosCadastroOrcamento> response = controller.atualizarOrcamento(dadosAtualizados);

        assertNotNull(response.getBody());
        assertEquals("água", response.getBody().nome());
        assertEquals(BigDecimal.valueOf(300), response.getBody().valor());
        assertEquals(Status.ABATIDO, response.getBody().status());
    }

    @Test
    @DisplayName("Deve retornar um status de conteúdo não encontrado")
    void excluirOrcamento() {
        mockOrcamentoPersistido();

        ResponseEntity<Void> response = controller.excluirOrcamento(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }


}