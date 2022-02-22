package ru.job4j.auth.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.auth.AuthApplication;
import ru.job4j.auth.testbeans.MetadataRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {AuthApplication.class})
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MetadataRepository md;

    @AfterEach
    void tearDown() {
        md.refreshTable();
    }

    @Test
    public void whenFindAll() throws Exception {
        mockMvc.perform(get("/person/"))
        .andExpect(content().json("[{\"id\":1,\"login\":\"admin\",\"password\":\"masterkey\"},{\"id\":2,\"login\":\"moderator\",\"password\":\"9999\"},{\"id\":3,\"login\":\"user\",\"password\":\"7777\"}]"))
        .andExpect(status().isOk());
    }

    @Test
    public void whenFindById() throws Exception {
        mockMvc.perform(get("/person/1"))
        .andExpect(content().json("{\"id\":1,\"login\":\"admin\",\"password\":\"masterkey\"}"))
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
        .andExpect(content().json("{\"id\":4,\"login\":\"velesov7493@gmail.com\",\"password\":\"password\"}"))
        .andExpect(status().isCreated());
    }

    @Test
    public void whenUpdate() throws Exception {
        mockMvc.perform(
            put("/person/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"id\":3,\"login\":\"velesov7493@yandex.ru\",\"password\":\"masterkey\"}")
        )
        .andExpect(status().isOk());
    }

    @Test
    public void whenDelete() throws Exception {
        mockMvc.perform(delete("/person/1"))
        .andExpect(status().isOk());
    }
}