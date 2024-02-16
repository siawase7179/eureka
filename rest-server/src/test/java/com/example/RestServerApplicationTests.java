package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;


@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestServerApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testNotFound() {
        webTestClient
            .get().uri("/v1")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is4xxClientError();
    }

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
