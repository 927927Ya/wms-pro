package com.d0dd.wms;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WarehouseListTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testListWithEmptyDate() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession();

        // Simulating ?createTime=&updateTime= which often happens with frontend forms
        mockMvc.perform(get("/api/warehouse/list")
                .session(session)
                .param("createTime", "")
                .param("updateTime", ""))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testReceiverMeHasPerms() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"收货员\",\"password\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession();

        mockMvc.perform(get("/api/auth/me")
                .session(session))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.perms").isArray())
                .andExpect(jsonPath("$.data.perms").value(org.hamcrest.Matchers.hasItem("inbound:receive")));
    }
}
