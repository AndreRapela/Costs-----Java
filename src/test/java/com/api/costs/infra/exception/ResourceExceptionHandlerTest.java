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
    @DisplayName("Deve retornar 404 quando EntityNotFoundException for lançada")
    void test404() throws Exception {
        mockMvc.perform(get("/fake/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource not found"))
                .andExpect(jsonPath("$.message").value("Recurso não encontrado no banco"))
                .andExpect(jsonPath("$.path").value("/fake/not-found"));
    }

    @Test
    @DisplayName("Deve retornar 422 quando ocorrer MethodArgumentNotValidException")
    void test422() throws Exception {
        mockMvc.perform(get("/fake/validation"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.message").value("Campo(s) invalido(s)"))
                .andExpect(jsonPath("$.path").value("/fake/validation"));
    }

    @Test
    @DisplayName("Deve retornar 400 quando ocorrer ConstraintViolationException")
    void test400() throws Exception {
        mockMvc.perform(get("/fake/constraint"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Invalid request params"))
                .andExpect(jsonPath("$.message").value("Parâmetros inválidos na requisição"))
                .andExpect(jsonPath("$.path").value("/fake/constraint"));
    }

    @Test
    @DisplayName("Deve retornar 409 quando ocorrer DataIntegrityViolationException")
    void test409() throws Exception {
        mockMvc.perform(get("/fake/integrity"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Data Integrity violation"))
                .andExpect(jsonPath("$.message", containsString("Operação não pode ser concluída")))
                .andExpect(jsonPath("$.path").value("/fake/integrity"));
    }

    @Test
    @DisplayName("Deve retornar 500 e incluir stackTrace quando ocorrer Exception genérica")
    void test500() throws Exception {
        mockMvc.perform(get("/fake/unexpected"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Unexpexted error"))
                .andExpect(jsonPath("$.message").value("Ocorreu um erro inesperado!"))
                .andExpect(jsonPath("$.path").value("/fake/unexpected"))
                .andExpect(jsonPath("$.stackTrace").exists())
                .andExpect(jsonPath("$.stackTrace", containsString("RuntimeException")))
                .andExpect(jsonPath("$.stackTrace", containsString("Erro inesperado no servidor")));
    }
}

}