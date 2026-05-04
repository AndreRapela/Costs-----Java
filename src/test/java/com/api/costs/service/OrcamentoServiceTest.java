package com.api.costs.service;

import com.api.costs.orcamento.Categoria;
import com.api.costs.orcamento.DTO.*;
import com.api.costs.orcamento.Orcamento;
import com.api.costs.orcamento.Status;
import com.api.costs.repository.OrcamentoRepository;
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
class OrcamentoServiceTest {

    @Mock
    OrcamentoRepository repository;

    @Mock
    UsuarioService usuarioService;

    @Mock
    Authentication authentication;

    @InjectMocks
    OrcamentoService service;


    private Usuario usuario;
    private Orcamento orcamento;
    private DadosCadastroOrcamentoAdmin dadosAdmin;
    private DadosCadastroOrcamento dados;
    private DadosAtulizarOrcamento dadosAtualizar;
    private Page<Orcamento> page;



    @BeforeEach
    void setUp() {

        usuario = new Usuario();
        usuario.setId(1L);
        dadosAdmin =  new DadosCadastroOrcamentoAdmin("orçamento teste", BigDecimal.valueOf(100), Categoria.CREDITO, Status.EXPIRADO,1L);
        orcamento = new Orcamento(dadosAdmin);
        orcamento.setId(10L);
        orcamento.setUsuario(usuario);

        dados = new DadosCadastroOrcamento(orcamento);

        dadosAtualizar = new DadosAtulizarOrcamento
                (10L,"Orcamento atualizado",BigDecimal.valueOf(200),Status.PENDENTE);
        page = new PageImpl<>(List.of(orcamento));

    }

    @Test
    void cadastrarOrcamentosPorUsuario() {
        when(usuarioService.getUsuarioLogado(authentication)).thenReturn(usuario);
        when(repository.save(any(Orcamento.class))).thenReturn(orcamento);

        Orcamento resultado = service.cadastrarOrcamentosPorUsuario(dados,authentication);

        assertAll("Validação do orcamento do usuario cadastrado",
                () -> assertNotNull(resultado),
                () -> assertEquals(orcamento.getId(), resultado.getId()),
                () -> assertEquals(usuario.getId(), resultado.getUsuario().getId()));

        verify(repository, times(1)).save(any(Orcamento.class));
        verify(usuarioService,times(1)).getUsuarioLogado(authentication);
    }

    @Test
    void cadastrarOrcamentos() {
        when(repository.save(any(Orcamento.class))).thenReturn(orcamento);

        Orcamento resultado = service.cadastrarOrcamentos(dadosAdmin);

        assertAll("Validação do orcamento cadastrado",
                () -> assertEquals(orcamento.getId(),resultado.getId()),
                () -> assertNotNull(resultado));

        verify(repository,times(1)).save(any(Orcamento.class));
    }

    @Test
    void listarOrcamentosPorUsuario() {
        when(usuarioService.getUsuarioLogado(authentication)).thenReturn(usuario);
        when(repository.findByUsuario(usuario,Pageable.unpaged())).thenReturn(page);

        Page<DadosListarOrcamento> resultado = service.listarOrcamentosPorUsuario(authentication,Pageable.unpaged());

        assertEquals(page.getTotalElements(),resultado.getTotalElements());
        verify(repository,times(1)).findByUsuario(usuario,Pageable.unpaged());
        verify(usuarioService,times(1)).getUsuarioLogado(authentication);

    }

    @Test
    void listarOrcamentos() {
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<DadosListarOrcamentoAdmin> resultado = service.listarOrcamentos(Pageable.unpaged());

        assertEquals(page.getTotalElements(),resultado.getTotalElements());
        verify(repository,times(1)).findAll(Pageable.unpaged());
    }

    @Test
    void buscarOrcamentoPorId() {
        when(repository.findById(10L)).thenReturn(Optional.of(orcamento));

        DadosListarOrcamentoAdmin resultado = service.buscarOrcamentoPorId(10L);

        assertEquals("orçamento teste",resultado.nome());
        verify(repository,times(1)).findById(10L);
    }

    @Test
    void buscarOrcamentoPorNomePorUsuario() {
        when(usuarioService.getUsuarioLogado(authentication)).thenReturn(usuario);
        when(repository.findByUsuarioAndNomeContaining(any(Usuario.class),anyString(),any(Pageable.class))).thenReturn(page);

        Page<DadosListarOrcamento> resultado = service.buscarOrcamentoPorNomePorUsuario
                (authentication,"orçamento teste", Pageable.unpaged());

        assertEquals(page.getTotalElements(), resultado.getTotalElements());
        verify(usuarioService,times(1)).getUsuarioLogado(authentication);
        verify(repository,times(1)).findByUsuarioAndNomeContaining(any(Usuario.class),anyString(),any(Pageable.class));
    }

    @Test
    void buscarOrcamentoPorNome() {
        when(repository.findByNomeContainingIgnoreCase(anyString(),any(Pageable.class))).thenReturn(page);

        Page<DadosListarOrcamentoAdmin> resultado = service.buscarOrcamentoPorNome("usuario Teste",Pageable.unpaged());

        assertEquals(page.getTotalElements(),resultado.getTotalElements());
        verify(repository,times(1)).findByNomeContainingIgnoreCase(anyString(),any(Pageable.class));
    }

    @Test
    void atualizarOrcamento() {
        when(repository.getReferenceById(anyLong())).thenReturn(orcamento);

        DadosListarOrcamentoAdmin resultado = service.atualizarOrcamento(dadosAtualizar);

        verify(repository,times(1)).getReferenceById(anyLong());
    }

    @Test
    void atulizarOrcamentoPorUsuario() {
        when(usuarioService.getUsuarioLogado(any(Authentication.class))).thenReturn(usuario);
        when(repository.findByUsuarioAndId(any(Usuario.class),anyLong())).thenReturn(Optional.of(orcamento));

        DadosListarOrcamento resultado = service.atulizarOrcamentoPorUsuario(authentication,dadosAtualizar);

        verify(usuarioService,times(1)).getUsuarioLogado(any(Authentication.class));
        verify(repository,times(1)).findByUsuarioAndId(any(Usuario.class),anyLong());
    }

    @Test
    void excluirOrcamento() {
        when(repository.getReferenceById(10L)).thenReturn(orcamento);

       service.excluirOrcamento(10L);

       assertFalse(orcamento.isAtivo());
       verify(repository,times(1)).getReferenceById(anyLong());
    }

    @Test
    void excluirOrcamentoPorUsuario() {
        when(usuarioService.getUsuarioLogado(any(Authentication.class))).thenReturn(usuario);
        when(repository.findByUsuarioAndId(any(Usuario.class),anyLong())).thenReturn(Optional.of(orcamento));

        service.excluirOrcamentoPorUsuario(authentication,10L);

        assertFalse(orcamento.isAtivo());

        verify(repository,times(1)).findByUsuarioAndId(any(Usuario.class),anyLong());
        verify(usuarioService,times(1)).getUsuarioLogado(any(Authentication.class));

    }
}