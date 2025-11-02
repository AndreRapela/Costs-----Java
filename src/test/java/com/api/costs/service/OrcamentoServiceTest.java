package com.api.costs.service;

import com.api.costs.orcamento.Categoria;
import com.api.costs.orcamento.DTO.DadosAtulizarOrcamento;
import com.api.costs.orcamento.DTO.DadosCadastroOrcamento;
import com.api.costs.orcamento.Orcamento;
import com.api.costs.orcamento.Status;
import com.api.costs.repository.OrcamentoRepository;
import com.api.costs.repository.UsuarioRepository;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrcamentoServiceTest {

    @Mock
    OrcamentoRepository repository;

    @Mock
    UsuarioService usuarioService;

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    Authentication authentication;

    @InjectMocks
    OrcamentoService service;


    private Usuario usuario;
    private Orcamento orcamento;
    private DadosCadastroOrcamento dadosCadastro;
    private DadosAtulizarOrcamento dadosAtualizar;
    private Page<Orcamento> page;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setId(1L);

        orcamento = new Orcamento(new DadosCadastroOrcamento
                ("orçamento teste", BigDecimal.valueOf(100), Categoria.CREDITO, Status.EXPIRADO,1L));
        orcamento.setId(10L);
        orcamento.setUsuario(usuario);

        dadosCadastro = new DadosCadastroOrcamento(orcamento);
        dadosAtualizar = new DadosAtulizarOrcamento
                (10L,"Orcamento atualizado",BigDecimal.valueOf(200),Status.PENDENTE);
        page = new PageImpl<>(List.of(orcamento));
    }

    @Test
    void cadastrarOrcamentosPorUsuario() {
        when(usuarioService.getUsuarioLogado(authentication)).thenReturn(usuario);
        when(repository.save(any(Orcamento.class))).thenReturn(orcamento);

        Orcamento resultado = service.cadastrarOrcamentosPorUsuario(dadosCadastro,authentication);

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

        Orcamento resultado = service.cadastrarOrcamentos(dadosCadastro);

        assertAll("Validação do orcamento cadastrado",
                () -> assertEquals(orcamento.getId(),resultado.getId()),
                () -> assertNotNull(resultado));

        verify(repository,times(1)).save(any(Orcamento.class));
    }

    @Test
    void listarOrcamentosPorUsuario() {
        when(usuarioService.getUsuarioLogado(authentication)).thenReturn(usuario);
        when(repository.findByUsuario(usuario,Pageable.unpaged())).thenReturn(page);

        Page<DadosCadastroOrcamento> resultado = service.listarOrcamentosPorUsuario(authentication, Pageable.unpaged());

        assertEquals(page.getTotalElements(),resultado.getTotalElements());
        verify(repository,times(1)).findByUsuario(usuario,Pageable.unpaged());
        verify(usuarioService,times(1)).getUsuarioLogado(authentication);

    }

    @Test
    void listarOrcamentos() {
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<DadosCadastroOrcamento> resultado = service.listarOrcamentos(Pageable.unpaged());

        assertEquals(page.getTotalElements(),resultado.getTotalElements());
        verify(repository,times(1)).findAll(Pageable.unpaged());
    }

    @Test
    void buscarOrcamentoPorId() {
        when(repository.getReferenceById(anyLong())).thenReturn(orcamento);

        DadosCadastroOrcamento resultado = service.buscarOrcamentoPorId(10L);

        assertEquals("orçamento teste",resultado.nome());
        verify(repository,times(1)).getReferenceById(10L);
    }

    @Test
    void buscarOrcamentoPorNomePorUsuario() {
        when(usuarioService.getUsuarioLogado(authentication)).thenReturn(usuario);
        when(repository.findByUsuarioAndNomeContaining(any(Usuario.class),anyString(),any(Pageable.class))).thenReturn(page);

        Page<DadosCadastroOrcamento> resultado = service.buscarOrcamentoPorNomePorUsuario
                (authentication,"orçamento teste", Pageable.unpaged());

        assertEquals(page.getTotalElements(), resultado.getTotalElements());
        verify(usuarioService,times(1)).getUsuarioLogado(authentication);
        verify(repository,times(1)).findByUsuarioAndNomeContaining(any(Usuario.class),anyString(),any(Pageable.class));
    }

    @Test
    void buscarOrcamentoPorNome() {
        when(repository.findByNome(anyString(),any(Pageable.class))).thenReturn(page);

        Page<DadosCadastroOrcamento> resultado = service.buscarOrcamentoPorNome("usuario Teste",Pageable.unpaged());

        assertEquals(page.getTotalElements(),resultado.getTotalElements());
        verify(repository,times(1)).findByNome(anyString(),any(Pageable.class));
    }

    @Test
    void atualizarOrcamento() {
        when(repository.getReferenceById(anyLong())).thenReturn(orcamento);

        DadosCadastroOrcamento resultado = service.atualizarOrcamento(dadosAtualizar);

        verify(repository,times(1)).getReferenceById(anyLong());
    }

    @Test
    void atulizarOrcamentoPorUsuario() {
        when(usuarioService.getUsuarioLogado(any(Authentication.class))).thenReturn(usuario);
        when(repository.findByUsuarioAndId(any(Usuario.class),anyLong())).thenReturn(Optional.of(orcamento));

        DadosCadastroOrcamento resultado = service.atulizarOrcamentoPorUsuario(authentication,dadosAtualizar);

        verify(usuarioService,times(1)).getUsuarioLogado(any(Authentication.class));
        verify(repository,times(1)).findByUsuarioAndId(any(Usuario.class),anyLong());
    }

    @Test
    void excluirOrcamento() {
        doNothing().when(repository).deleteById(anyLong());

       service.excluirOrcamento(10L);

       verify(repository,times(1)).deleteById(anyLong());
    }

    @Test
    void excluirOrcamentoPorUsuario() {
        when(usuarioService.getUsuarioLogado(any(Authentication.class))).thenReturn(usuario);
        when(repository.findByUsuarioAndId(any(Usuario.class),anyLong())).thenReturn(Optional.of(orcamento));
        doNothing().when(repository).delete(any(Orcamento.class));

        service.excluirOrcamentoPorUsuario(authentication,10L);

        verify(repository,times(1)).findByUsuarioAndId(any(Usuario.class),anyLong());
        verify(usuarioService,times(1)).getUsuarioLogado(any(Authentication.class));
        verify(repository,times(1)).delete(any(Orcamento.class));
    }
}