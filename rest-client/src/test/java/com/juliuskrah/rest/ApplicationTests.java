package com.juliuskrah.rest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import lombok.SneakyThrows;

@WebMvcTest(controllers = Application.class)
public class ApplicationTests {
    @Autowired
    private MockMvc mvc;

    @Test
    @SneakyThrows
    void testRetrieveService() {
        this.mvc.perform(post("/merchant/service").content("{}")) //
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ResponseDescription", is("The charge request was successful")));
    }
}
