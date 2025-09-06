package com.api.costs.infra.exception;

import com.api.costs.controllers.OrcamentoController;
import com.api.costs.infra.TokenService;
import com.api.costs.orcamento.repository.OrcamentoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ResourceExceptionHandlerTest {

    @Autowired
    MockMvc mock;

    @Test
    @DisplayName("Deve retornar erro 404 quando o recurso não for encontrado")
    void error404() throws Exception {
        mock.perform(get("/costs/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource not found"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").exists());

    }

    @Test
    @DisplayName("Deve retornar 400 e lista de erros de validação quando dados inválidos forem enviados")
    void error400() {
    }
}