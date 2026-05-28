package com.d0dd.wms;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OpenApiGeneratorTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void generateOpenApiJson() throws Exception {
        MvcResult result = mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonContent = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Files.write(Paths.get("openapi.json"), jsonContent.getBytes(StandardCharsets.UTF_8));
        System.out.println("OpenAPI JSON generated at: " + Paths.get("openapi.json").toAbsolutePath());
    }
}
