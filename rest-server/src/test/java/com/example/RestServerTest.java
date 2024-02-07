package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication; // 추가
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.resource.RestServerController;

@WebMvcTest(RestServerController.class)
public class RestServerTest {

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp(ApplicationContext context) {
        webTestClient = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    public void test() {
        webTestClient
            .get().uri("/v1")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is4xxClientError();
    }

    @SpringBootApplication // 추가
    static class TestConfiguration {
    }
}
