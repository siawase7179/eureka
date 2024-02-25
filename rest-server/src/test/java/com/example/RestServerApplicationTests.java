package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;


@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class RestServerApplicationTests {

    private final WebTestClient webTestClient;
    
    @Autowired
    public RestServerApplicationTests(WebTestClient webTestClient){
        this.webTestClient = webTestClient;
    }

    @DisplayName("Not Found")
    @Test
    public void testNotFound() {
        webTestClient
            .get().uri("/v1")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is4xxClientError();
    }

    @DisplayName("Requset Token")
    @Test
    public void testRequestTokne(){
        webTestClient
        .post().uri("/v1/token")
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-Client-Id", "clientId")
        .header("X-Client-Password", "clientPassword")
        .exchange()
        .expectStatus().isOk();
    }
}
