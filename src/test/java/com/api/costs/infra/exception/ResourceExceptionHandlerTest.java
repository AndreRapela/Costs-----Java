package com.api.costs.infra.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @DisplayName("Deve retornar 400 e lista de erros de validação quando dados inválidos forem enviados ao cadastrar")
    void error400AoCadastrar() throws Exception {
        String valorVazio = "{}";

        mock.perform(post("/costs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valorVazio))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].campo").exists())
                .andExpect(jsonPath("$[0].mensagem").exists());


    }

    @Test
    @DisplayName("Deve retornar 400 e lista de erros de validação quando dados inválidos forem enviados ao atualizar")
    void error400AoAtualizar() throws Exception {
        String valorVazio = "{}";

        mock.perform(put("/costs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(valorVazio))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("[0].campo").exists())
                .andExpect(jsonPath("[0].mensagem").exists());


    }
}