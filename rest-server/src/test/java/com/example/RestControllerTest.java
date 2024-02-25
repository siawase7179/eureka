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

import com.example.feign.AuthorizeService;

@ActiveProfiles("test")
@WebMvcTest(RestController.class)
public class RestControllerTest {
    @MockBean
    private AuthorizeService authorizeService;

    @Autowired
    private MockMvc mockMvc;
    
    @Test
    public void Test() throws Exception{
        
        ResultActions actions = mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/token")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Client-Id", "id")
                .header("X-Client-Password", "password")
        );
        actions.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    
}
