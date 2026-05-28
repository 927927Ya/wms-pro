package com.d0dd.wms;

import com.d0dd.wms.entity.Warehouse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class WarehouseUniquenessTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testDuplicateNameOnCreate() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession();

        // 1. Create a warehouse
        Warehouse w1 = new Warehouse();
        w1.setWarehouseName("UniqueWarehouse1");
        w1.setWarehouseCode("W001");
        w1.setLength(new java.math.BigDecimal("100"));
        w1.setWidth(new java.math.BigDecimal("100"));
        
        mockMvc.perform(post("/api/warehouse")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(w1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        // 2. Try to create another with same name
        Warehouse w2 = new Warehouse();
        w2.setWarehouseName("UniqueWarehouse1"); // Same name
        w2.setWarehouseCode("W002");
        
        mockMvc.perform(post("/api/warehouse")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(w2)))
                .andExpect(status().isOk()) // Global handler catches it, returns 200 OK HTTP but 500 code in body
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("仓库名称已存在"));
    }

    @Test
    public void testDuplicateNameOnUpdate() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession();

        // 1. Create warehouse A
        Warehouse w1 = new Warehouse();
        w1.setWarehouseName("WarehouseA");
        mockMvc.perform(post("/api/warehouse")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(w1)))
                .andExpect(status().isOk());

        // 2. Create warehouse B
        Warehouse w2 = new Warehouse();
        w2.setWarehouseName("WarehouseB");
        mockMvc.perform(post("/api/warehouse")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(w2)))
                .andExpect(status().isOk());

        MvcResult listResult = mockMvc.perform(get("/api/warehouse/list")
                .session(session)
                .param("warehouseName", "WarehouseB"))
                .andExpect(status().isOk())
                .andReturn();

        String content = listResult.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(content);
        JsonNode dataNode = rootNode.path("data");
        Long warehouseBId = dataNode.get(0).get("id").asLong();

        Warehouse update = new Warehouse();
        update.setId(warehouseBId);
        update.setWarehouseName("WarehouseA");

        mockMvc.perform(put("/api/warehouse")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("仓库名称已存在"));
    }
}
