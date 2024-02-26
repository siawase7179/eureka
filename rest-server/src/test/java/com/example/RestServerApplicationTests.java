package com.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.example.config.Config;
import com.example.feign.model.AccountInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.tomakehurst.wiremock.client.WireMock.*;


@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 9090)
@ActiveProfiles("test")
@SpringBootTest(
    webEnvironment = WebEnvironment.MOCK,
    properties = {
        
    }
)
public class RestServerApplicationTests {
    private ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private Config config;
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private WireMockServer mockServer;

    @BeforeEach
    public void before(TestInfo testInfo) throws JsonProcessingException{
        
        if (testInfo.getDisplayName().equals("test_auth_is_ok")){
            stubFor(post("/auth")
                    .withRequestBody(equalToJson(
                        mapper.writeValueAsString(
                            AccountInfo.builder()
                                    .clientId("id")
                                    .clientPassword("pass")
                                .build()
                        )
                    ))
                    .willReturn(jsonResponse(
                        mapper.writeValueAsString(
                            AccountInfo.builder()
                                    .clientId("id")
                                    .clientPassword("pass")
                                .build()
                        ),
                        HttpStatus.OK.value()))
            );
        }else if(testInfo.getDisplayName().equals("test_auth_is_not_found")){
            stubFor(post("/auth")
                .willReturn(aResponse()
                    .withStatus(HttpStatus.NOT_FOUND.value())
                )
            );
        }else{
            stubFor(post("/auth")
                .willReturn(aResponse()
                    .withStatus(HttpStatus.BAD_REQUEST.value())
                )
            );
        }
       
        mockServer.start();
    }

    @AfterEach
    public void after(){
        mockServer.stop();
    }

    @Test
    public void test() throws Exception{
        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isNotFound());
    }

    @Test
    public void test_invalid_header_client_id() throws Exception{
        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/token")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Client-Id", "id")
        ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("code").isNotEmpty())
        .andExpect(jsonPath("code").value("90001"));
    }

    @Test
    public void test_invalid_header_client_password() throws Exception{
        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/token")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Client-Password", "pass")
        ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("code").isNotEmpty())
        .andExpect(jsonPath("code").value("90001"));
    }

    @Test
    @DisplayName("test_auth_is_ok")
    public void test_auth_is_ok() throws Exception{
        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/token")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Client-Id", "id")
                .header("X-Client-Password", "pass")
        )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("code").value("0000"));
    }

    @Test
    @DisplayName("test_auth_is_not_found")
    public void test_auth_is_not_found() throws Exception{
        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/token")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Client-Id", "id")
                .header("X-Client-Password", "pass")
        )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("test_auth_server_error")
    public void test_auth_server_error() throws Exception{
        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/token")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Client-Id", "id")
                .header("X-Client-Password", "pass")
        )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isInternalServerError());
    }
    private String token = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6ImlkIiwiZXhwIjoxNzA4OTI5NDExfQ.jTrcLgHS5ZWLx1dgbBeBHa6etPO7k91GMoY8Ui3JMp0";
    
    @Test
    public void test_expired_jwt_token() throws Exception{
        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/request")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " +token)
        ).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("code").value("90003"));
    }

    @Test
    public void test_invalid_jwt_token() throws Exception{
        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/request")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
        )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("code").value("90004"));
    }

    
}
