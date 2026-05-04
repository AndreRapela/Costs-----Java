package com.api.costs.controllers;

import com.api.costs.infra.SecurityConfiguration;
import com.api.costs.infra.SecurityFilter;
import com.api.costs.infra.TokenService;
import com.api.costs.orcamento.Categoria;
import com.api.costs.orcamento.DTO.*;
import com.api.costs.orcamento.Orcamento;
import com.api.costs.orcamento.Status;
import com.api.costs.service.OrcamentoService;
import com.api.costs.usuario.Usuario;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = OrcamentoController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class OrcamentoControllerTest {

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

   @MockBean
    private OrcamentoService service;

    @MockBean
    private SecurityFilter securityFilter;

    @MockBean
    private JpaMetamodelMappingContext jpaMappingContext;


    private DadosCadastroOrcamentoAdmin dadosAdmin;
    private DadosCadastroOrcamento dados;
    private  Orcamento orcamento;
    private DadosListarOrcamento dadosListar;
    private DadosListarOrcamentoAdmin dadosListarAdmin;
    private DadosAtulizarOrcamento dadosAtualizados;
    private Usuario usuario;
    private Page<DadosListarOrcamento> page;
    private Page<DadosListarOrcamentoAdmin> pageAdmin;

    @BeforeEach
    void setup(){
        usuario = new Usuario();
        usuario.setId(1L);
        dados = new DadosCadastroOrcamento
                ( "energia", BigDecimal.valueOf(200), Categoria.DEBITO, Status.PENDENTE);
        orcamento = new Orcamento(dados);
        orcamento.setId(1L);
        orcamento.setUsuario(usuario);
        dadosListar = new DadosListarOrcamento(orcamento);
        dadosListarAdmin = new DadosListarOrcamentoAdmin(orcamento);
        dadosAdmin = new DadosCadastroOrcamentoAdmin(orcamento);
        dadosAtualizados = new DadosAtulizarOrcamento(1L, "água", BigDecimal.valueOf(300), Status.ABATIDO);
        page = new PageImpl<>(List.of(dadosListar));
        pageAdmin = new PageImpl<>(List.of(dadosListarAdmin));
    }


//    @Test
//    @DisplayName("Deve retornar o status 201 quando cadastrar um orçamento do usuário logado")
//    void cadastrarOrcamentoPorUsuario(){
//        when(service.cadastrarOrcamentosPorUsuario(any(DadosCadastroOrcamento.class),any(Authentication.class)))
//                .thenReturn(orcamento);
//        when(authentication.getPrincipal()).thenReturn(usuario);
//        ResponseEntity<DadosCadastroOrcamento> response = controller.cadastrarOrcamentosPorUsuario(dados,authentication);
//
//        assertAll("validação dos campos do orçamento retornado",
//                () -> assertEquals(1L,((Usuario) authentication.getPrincipal()).getId()),
//                () -> assertEquals("energia", response.getBody().nome()),
//                () -> assertEquals(BigDecimal.valueOf(200), response.getBody().valor()),
//                () -> assertEquals(Categoria.DEBITO, response.getBody().categoria()),
//                () -> assertEquals(Status.PENDENTE, response.getBody().status()),
//                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
//                () -> assertNotNull(response.getHeaders().getLocation()));
//    }
//

    @Test
    @DisplayName("Deve retornar o status 201 created quando cadastrar um orçamento com sucesso")
    void cadastrarOrcamentos() throws Exception{
        when(service.cadastrarOrcamentos(any(DadosCadastroOrcamentoAdmin.class))).thenReturn(orcamento);

        mockMvc.perform(post("/costs/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dadosAdmin)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("energia"))
                .andExpect(jsonPath("$.valor").value(200))
                .andExpect(jsonPath("$.categoria").value("DEBITO"))
                .andExpect(jsonPath("$.status").value("PENDENTE"));

    }



//    @Test
//    @DisplayName("Deve listar os orçamentos do usuário logado (200 OK)")
//    void listarOrcamentosPorUsuario() throws Exception {
//        when(service.listarOrcamentosPorUsuario(any(Authentication.class),any(Pageable.class))).thenReturn(page);
//
//        mockMvc.perform(get("/costs"))
//                .andExpect(status().isOk());
//    }


    @Test
    @DisplayName("Deve listar todos os orçamentos para o adm com sucesso (200 OK)")
    void listarOrcamento() throws Exception {
        when(service.listarOrcamentos(any(Pageable.class))).thenReturn(pageAdmin);

       mockMvc.perform(get("/costs/admin"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content[0].nome").value("energia"));
    }


    @Test
    @DisplayName("Deve retornar orçamento escolhido pelo id!")
    void BuscarOrcamentoPorId() throws Exception {
        when(service.buscarOrcamentoPorId(anyLong())).thenReturn(dadosListarAdmin);

        mockMvc.perform(get("/costs/admin/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("energia"));
    }


    @Test
    @DisplayName("Deve retornar os orçamentos buscado por nome")
    void buscarOrcamentoPorNome() throws Exception {
        when(service.buscarOrcamentoPorNome(anyString(),any(Pageable.class))).thenReturn(pageAdmin);

        mockMvc.perform(get("/costs/admin/find")
                .param("nome","energia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("energia"));

    }

//
//    @Test
//    @DisplayName("Deve retornar os orçamentos buscados por nome do próprio usuário")
//    void buscarOrcamentoPorNomePorUsuario(){
//        when(service.buscarOrcamentoPorNomePorUsuario(any(Authentication.class),anyString(),any(Pageable.class))).thenReturn(page);
//
//        ResponseEntity<Page<DadosListarOrcamento>> response =
//                controller.buscarOrcamentoPorUsuarioPorNome(authentication,"energia",Pageable.unpaged());
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals(page.getTotalElements(), response.getBody().getTotalElements());
//    }
//
//
    @Test
    @DisplayName("Deve retornar os valores atualizados do orçamento passado")
    void atualizarOrcamento() throws Exception{
        orcamento.setNome(dadosAtualizados.nome());
        orcamento.setValor(dadosAtualizados.valor());
        orcamento.setStatus(dadosAtualizados.status());

        when(service.atualizarOrcamento(any(DadosAtulizarOrcamento.class)))
                .thenReturn(new DadosListarOrcamentoAdmin(orcamento));

       mockMvc.perform(put("/costs/admin")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(dadosAtualizados)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nome").value("água"))
               .andExpect(jsonPath("$.valor").value(300))
               .andExpect(jsonPath("$.status").value("ABATIDO"));

    }


//    @Test
//    @DisplayName("Deve retornar os valores atualizados do orçamento passado do proprio usuário")
//    void atualizarOrcamentoPorUsuario(){
//        when(service.atulizarOrcamentoPorUsuario(any(Authentication.class),any(DadosAtulizarOrcamento.class))).thenReturn(dadosListar);
//
//        ResponseEntity<DadosListarOrcamento> response = controller.atualizarOrcamentoPorUsuario(authentication,dadosAtualizados);
//
//        assertAll("Validação dos campos atualizados",
//                () ->assertNotNull(response.getBody()),
//                () -> assertEquals(retorno.nome(), response.getBody().nome()),
//                () -> assertEquals(retorno.valor(), response.getBody().valor()),
//                () -> assertEquals(retorno.status(), response.getBody().status()));
//    }
//

    @Test
    @DisplayName("exclui um orçamento")
    void excluirOrcamento() throws Exception{
        doNothing().when(service).excluirOrcamento(anyLong());

        mockMvc.perform(delete("/costs/admin/1"))
                .andExpect(status().isNoContent());
    }

//
//    @Test
//    @DisplayName("Exclui um orçamento do usuário")
//    void excluirOrcamentoPorUsuario() {
//        doNothing().when(service).excluirOrcamentoPorUsuario(any(Authentication.class),anyLong());
//
//        ResponseEntity<Void> response = controller.excluirOrcamentoPorUsuario(authentication,1L);
//
//        assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());
//        assertNull(response.getBody());
//    }
//

}