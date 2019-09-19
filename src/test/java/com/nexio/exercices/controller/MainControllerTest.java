package com.nexio.exercices.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldShowHomePage() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(forwardedUrl("/WEB-INF/views/home.jsp"));
    }

    @Test
    public void shouldReturnNotFound() throws Exception {
        this.mockMvc.perform(get("/unexistent-page"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldShowError500PageIfProductIdIsInvalid() throws Exception {
        final ResponseEntity<String> result = this.restTemplate.getForEntity(
                String.format("http://localhost:%d/products/stringid/details", port),
                String.class);

        assertThat(result.getStatusCodeValue()).isEqualTo(400);
        assertThat(result.getBody()).contains("Erreur inattendue");
    }
}