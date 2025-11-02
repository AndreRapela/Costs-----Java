package com.api.costs.infra.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;


import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ResourceExceptionHandlerTest {

    @Autowired
    MockMvc mock;


    private ResultMatcher[] defaultErrorExpectations(int status, String error, String message, String path){
        return new ResultMatcher[]{
                jsonPath("$.status").value(status),
                jsonPath("$.error").value(error),
                jsonPath("$.message").value(message),
                jsonPath("$.path").value(path)
        };
    }


    @Test
    @DisplayName("Deve retornar 404 quando EntityNotFoundException for lançada")
    void teste404() throws Exception {
        mock.perform(get("/fake/not-found"))
                .andExpect(status().isNotFound())
                .andExpectAll(defaultErrorExpectations(404,"Resource not found",
                        "Recurso não encontrado no banco","/fake/not-found"));

    }

    @Test
    @DisplayName("Deve retornar 422 quando ocorrer MethodArgumentNotValidException")
    void teste422() throws Exception {
        mock.perform(get("/fake/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpectAll(defaultErrorExpectations(422,"Validation failed",
                        "Campo(s) invalido(s)","/fake/validation"));
    }

    @Test
    @DisplayName("Deve retornar 400 quando ocorrer ConstraintViolationException")
    void test400() throws Exception {
        mock.perform(get("/fake/constraint"))
                .andExpect(status().isBadRequest())
                .andExpectAll(defaultErrorExpectations(400,"Invalid request params",
                        "Parâmetros invalidos na requisição", "/fake/constraint"));
    }


    @Test
    @DisplayName("Deve retornar 409 quando ocorrer DataIntegrityViolationException")
    void test409() throws Exception {
        mock.perform(get("/fake/integrity"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Data Integrity violation"))
                .andExpect(jsonPath("$.message", containsString("Operação não pode ser concluída")))
                .andExpect(jsonPath("$.path").value("/fake/integrity"));

    }


    @Test
    @DisplayName("Deve retornar 500 e incluir stackTrace quando ocorrer Exception genérica")
    void test500() throws Exception {
        mock.perform(get("/fake/unexpected"))
                .andExpect(status().isInternalServerError())
                .andExpectAll(defaultErrorExpectations(500,"Unexpected error!!!!!",
                        "Ocorreu um erro inesperado!","/fake/unexpected"))
                .andExpect(jsonPath("$.stackTrace").exists())
                .andExpect(jsonPath("$.stackTrace",containsString("RuntimeException")))
                .andExpect(jsonPath("$.stackTrace",containsString("Erro inesperado no servidor!!!!!")));
}

}