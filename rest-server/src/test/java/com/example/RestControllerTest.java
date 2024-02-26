package com.example;


import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;

import static org.mockito.BDDMockito.given;

import com.example.exception.handler.RestExceptionHandler;
import com.example.feign.AuthorizeService;
import com.example.service.TokenService;

@ActiveProfiles("test")
@WebMvcTest({
    RestController.class,
})
public class RestControllerTest {
    @MockBean
    private AuthorizeService authorizeService;

    @MockBean
    private TokenService tokenService;

    @Autowired
    private MockMvc mockMvc;

    private String token = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6ImlkIiwiZXhwIjoxNzA4OTI5NDExfQ.jTrcLgHS5ZWLx1dgbBeBHa6etPO7k91GMoY8Ui3JMp0";
    @Test
    public void test_invalid_jwt_token() throws Exception{
        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/request")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
        )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest());
    }
}
