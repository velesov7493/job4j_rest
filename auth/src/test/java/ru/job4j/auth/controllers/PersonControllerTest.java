package ru.job4j.auth.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.auth.AuthApplication;
import ru.job4j.auth.testbeans.MetadataRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {AuthApplication.class})
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MetadataRepository md;
    @Autowired
    private BCryptPasswordEncoder encoder;

    private String token;

    @BeforeAll
    public void setup() throws Exception {
        token = mockMvc.perform(
            post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\": \"admin\", \"password\": \"masterkey\"}")
        )
        .andReturn().getResponse().getHeader("Authorization");
    }

    @AfterEach
    void tearDown() {
        md.refreshTable();
    }

    @Test
    public void whenFindAll() throws Exception {
        mockMvc.perform(
                get("/person/")
                .header("Authorization", token)
        )
        .andExpect(content().json("[{\"id\":1,\"login\":\"admin\",\"password\":\"$2a$10$L/PD8CD1zL4nUSpWAErF4O4YrdIgzJegdRQJ4EAwX9LjHEQ.FniVe\", \"enabled\": true, \"authorityNames\":[\"ROLE_USER\",\"ROLE_STAFF\",\"ROLE_ADMIN\"]},{\"id\":2,\"login\":\"moderator\",\"password\":\"$2a$10$NYucCa6Klha/GNlDiOnpaeU/YNw93PSpWvPcCJecg91cwdvE0ctnW\", \"enabled\": true, \"authorityNames\":[\"ROLE_USER\",\"ROLE_STAFF\"]},{\"id\":3,\"login\":\"user\",\"password\":\"$2a$10$cEQ.BPEGg/Oz8MZQ1nVQE.XhM8xnV/.XrYhjo.D.Vqky3XyiJe0CO\", \"enabled\": true, \"authorityNames\":[\"ROLE_USER\"]},{\"id\":4,\"login\":\"chat-service\",\"password\":\"$2a$10$h2p4jQLR4z.m2PcNO0Yl7.tH8/EBDWRouu3oFEjjPUNmUhlZW9AKi\", \"enabled\": true, \"authorityNames\":[\"ROLE_ADMIN\"]},{\"id\":5,\"login\":\"employees-service\",\"password\":\"$2a$10$h2p4jQLR4z.m2PcNO0Yl7.tH8/EBDWRouu3oFEjjPUNmUhlZW9AKi\", \"enabled\": true, \"authorityNames\":[\"ROLE_ADMIN\"]}]"))
        .andExpect(status().isOk());
    }

    @Test
    public void whenFindById() throws Exception {
        mockMvc.perform(
                get("/person/1")
                .header("Authorization", token)
        )
        .andExpect(content().json("{\"id\":1,\"login\":\"admin\",\"password\":\"$2a$10$L/PD8CD1zL4nUSpWAErF4O4YrdIgzJegdRQJ4EAwX9LjHEQ.FniVe\", \"enabled\": true, \"authorityNames\":[\"ROLE_USER\",\"ROLE_STAFF\",\"ROLE_ADMIN\"]}"))
        .andExpect(status().isOk());
    }

    @Test
    public void whenCreate() throws Exception {
        mockMvc.perform(
            post("/person/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"login\":\"velesov7493@gmail.com\",\"password\":\"password\"}")
        )
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
    }

    @Test
    public void whenUpdate() throws Exception {
        mockMvc.perform(
            put("/person/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"id\":3,\"login\":\"velesov7493@yandex.ru\",\"password\":\"masterkey\"}")
            .header("Authorization", token)
        )
        .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void whenDelete() throws Exception {
        mockMvc.perform(
                delete("/person/1")
                .header("Authorization", token)
        )
        .andExpect(status().isOk());
    }
}