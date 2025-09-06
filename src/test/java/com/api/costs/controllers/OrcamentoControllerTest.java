package com.api.costs.controllers;

import com.api.costs.orcamento.Categoria;
import com.api.costs.orcamento.DTO.DadosCadastroOrcamento;
import com.api.costs.orcamento.Orcamento;
import com.api.costs.orcamento.Status;
import com.api.costs.orcamento.repository.OrcamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class OrcamentoControllerTest {

    @Mock
    private OrcamentoRepository repository;

    private DadosCadastroOrcamento dados;

    @InjectMocks
    OrcamentoController controller;

//    private Orcamento createOrcamento(DadosCadastroOrcamento dados){
//        Orcamento newOrcamento = new Orcamento(dados);
//        this.entityManager.persist(newOrcamento);
//        return newOrcamento;
//    }

    @BeforeEach
    void CriarDTO(){
        MockitoAnnotations.openMocks(this);
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        dados = new DadosCadastroOrcamento
                ( "energia",  200d, Categoria.DEBITO, Status.PENDENTE);
    }

    private ResponseEntity<Orcamento> cadastrarMock(long id){
        Orcamento orcamentoPersistido = new Orcamento(dados);
        orcamentoPersistido.setId(id);

        when(repository.save(any(Orcamento.class))).thenReturn(orcamentoPersistido);
        return controller.cadastrar(dados);
    }

    @Test
    @DisplayName("Deve retornar o status 201 created quando cadastrar com sucesso")
    void cadastrarComSucesso() {

        ResponseEntity<Orcamento> response = cadastrarMock(1L);

        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve retornar o orçamento cadastrado com os valores corretos")
    void cadastrarValidadeCampos(){
        ResponseEntity<Orcamento> response = cadastrarMock(1L);

        Orcamento orcamento = response.getBody();

        assertNotNull(orcamento.getId());
        assertEquals("energia",orcamento.getNome());
        assertEquals(200,orcamento.getValor());
        assertEquals(Categoria.DEBITO,orcamento.getCategoria());
        assertEquals(Status.PENDENTE, orcamento.getStatus());
    }

    @Test
    void listarOrcamento() {
    }

    @Test
    void orcamentoPorId() {
    }

    @Test
    void atualizarOrcamento() {
    }

    @Test
    void excluirOrcamento() {
    }


}