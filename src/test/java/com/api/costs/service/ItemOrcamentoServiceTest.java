package com.api.costs.service;

import com.api.costs.itemOrcamento.Categoria;
import com.api.costs.itemOrcamento.DTO.*;
import com.api.costs.itemOrcamento.ItemOrcamento;
import com.api.costs.itemOrcamento.Status;
import com.api.costs.repository.ItemOrcamentoRepository;
import com.api.costs.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemOrcamentoServiceTest {

    @Mock
    ItemOrcamentoRepository repository;

    @Mock
    UsuarioService usuarioService;

    @Mock
    Authentication authentication;

    @InjectMocks
    ItemOrcamentoService service;


    private Usuario usuario;
    private ItemOrcamento itemOrcamento;
    private DadosCadastroItemOrcamentoAdmin dadosAdmin;
    private DadosCadastroItemOrcamento dados;
    private DadosAtulizarItemOrcamento dadosAtualizar;
    private Page<ItemOrcamento> page;



    @BeforeEach
    void setUp() {

        usuario = new Usuario();
        usuario.setId(1L);
        dadosAdmin =  new DadosCadastroItemOrcamentoAdmin("orçamento teste", BigDecimal.valueOf(100), Categoria.CREDITO, Status.EXPIRADO,1L);
        itemOrcamento = new ItemOrcamento(dadosAdmin);
        itemOrcamento.setId(10L);
        itemOrcamento.setUsuario(usuario);

        dados = new DadosCadastroItemOrcamento(itemOrcamento);

        dadosAtualizar = new DadosAtulizarItemOrcamento
                (10L,"ItemOrcamento atualizado",BigDecimal.valueOf(200),Status.PENDENTE);
        page = new PageImpl<>(List.of(itemOrcamento));

    }

    @Test
    void cadastrarOrcamentosPorUsuario() {
        when(usuarioService.getUsuarioLogado(authentication)).thenReturn(usuario);
        when(repository.save(any(ItemOrcamento.class))).thenReturn(itemOrcamento);

        ItemOrcamento resultado = service.cadastrarItemOrcamentosPorUsuario(dados,authentication);

        assertAll("Validação do itemOrcamento do usuario cadastrado",
                () -> assertNotNull(resultado),
                () -> assertEquals(itemOrcamento.getId(), resultado.getId()),
                () -> assertEquals(usuario.getId(), resultado.getUsuario().getId()));

        verify(repository, times(1)).save(any(ItemOrcamento.class));
        verify(usuarioService,times(1)).getUsuarioLogado(authentication);
    }

    @Test
    void cadastrarOrcamentos() {
        when(repository.save(any(ItemOrcamento.class))).thenReturn(itemOrcamento);

        ItemOrcamento resultado = service.cadastrarItemOrcamentos(dadosAdmin);

        assertAll("Validação do itemOrcamento cadastrado",
                () -> assertEquals(itemOrcamento.getId(),resultado.getId()),
                () -> assertNotNull(resultado));

        verify(repository,times(1)).save(any(ItemOrcamento.class));
    }

    @Test
    void listarOrcamentosPorUsuario() {
        when(usuarioService.getUsuarioLogado(authentication)).thenReturn(usuario);
        when(repository.findByUsuario(usuario,Pageable.unpaged())).thenReturn(page);

        Page<DadosListarItemOrcamento> resultado = service.listarItemOrcamentosPorUsuario(authentication,Pageable.unpaged());

        assertEquals(page.getTotalElements(),resultado.getTotalElements());
        verify(repository,times(1)).findByUsuario(usuario,Pageable.unpaged());
        verify(usuarioService,times(1)).getUsuarioLogado(authentication);

    }

    @Test
    void listarOrcamentos() {
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<DadosListarItemOrcamentoAdmin> resultado = service.listarItemOrcamentos(Pageable.unpaged());

        assertEquals(page.getTotalElements(),resultado.getTotalElements());
        verify(repository,times(1)).findAll(Pageable.unpaged());
    }

    @Test
    void buscarOrcamentoPorId() {
        when(repository.findById(10L)).thenReturn(Optional.of(itemOrcamento));

        DadosListarItemOrcamentoAdmin resultado = service.buscarItemOrcamentoPorId(10L);

        assertEquals("orçamento teste",resultado.nome());
        verify(repository,times(1)).findById(10L);
    }

    @Test
    void buscarOrcamentoPorNomePorUsuario() {
        when(usuarioService.getUsuarioLogado(authentication)).thenReturn(usuario);
        when(repository.findByUsuarioAndNomeContainingIgnoreCase(any(Usuario.class),anyString(),any(Pageable.class))).thenReturn(page);

        Page<DadosListarItemOrcamento> resultado = service.buscarItemOrcamentoPorNomePorUsuario
                (authentication,"orçamento teste", Pageable.unpaged());

        assertEquals(page.getTotalElements(), resultado.getTotalElements());
        verify(usuarioService,times(1)).getUsuarioLogado(authentication);
        verify(repository,times(1)).findByUsuarioAndNomeContainingIgnoreCase(any(Usuario.class),anyString(),any(Pageable.class));
    }

    @Test
    void buscarOrcamentoPorNome() {
        when(repository.findByNomeContainingIgnoreCase(anyString(),any(Pageable.class))).thenReturn(page);

        Page<DadosListarItemOrcamentoAdmin> resultado = service.buscarItemOrcamentoPorNome("usuario Teste",Pageable.unpaged());

        assertEquals(page.getTotalElements(),resultado.getTotalElements());
        verify(repository,times(1)).findByNomeContainingIgnoreCase(anyString(),any(Pageable.class));
    }

    @Test
    void atualizarOrcamento() {
        when(repository.getReferenceById(anyLong())).thenReturn(itemOrcamento);

        DadosListarItemOrcamentoAdmin resultado = service.atualizarItemOrcamento(dadosAtualizar);

        verify(repository,times(1)).getReferenceById(anyLong());
    }

    @Test
    void atulizarOrcamentoPorUsuario() {
        when(usuarioService.getUsuarioLogado(any(Authentication.class))).thenReturn(usuario);
        when(repository.findByUsuarioAndId(any(Usuario.class),anyLong())).thenReturn(Optional.of(itemOrcamento));

        DadosListarItemOrcamento resultado = service.atulizarItemOrcamentoPorUsuario(authentication,dadosAtualizar);

        verify(usuarioService,times(1)).getUsuarioLogado(any(Authentication.class));
        verify(repository,times(1)).findByUsuarioAndId(any(Usuario.class),anyLong());
    }

    @Test
    void excluirOrcamento() {
        when(repository.getReferenceById(10L)).thenReturn(itemOrcamento);

       service.excluirItemOrcamento(10L);

       assertFalse(itemOrcamento.isAtivo());
       verify(repository,times(1)).getReferenceById(anyLong());
    }

    @Test
    void excluirOrcamentoPorUsuario() {
        when(usuarioService.getUsuarioLogado(any(Authentication.class))).thenReturn(usuario);
        when(repository.findByUsuarioAndId(any(Usuario.class),anyLong())).thenReturn(Optional.of(itemOrcamento));

        service.excluirItemOrcamentoPorUsuario(authentication,10L);

        assertFalse(itemOrcamento.isAtivo());

        verify(repository,times(1)).findByUsuarioAndId(any(Usuario.class),anyLong());
        verify(usuarioService,times(1)).getUsuarioLogado(any(Authentication.class));

    }
}