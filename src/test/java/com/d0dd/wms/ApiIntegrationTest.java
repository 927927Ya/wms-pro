package com.d0dd.wms;

import com.d0dd.wms.dto.InboundDTO;
import com.d0dd.wms.entity.Warehouse;
import com.fasterxml.jackson.core.type.TypeReference;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testWarehouseLifecycle() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession();

        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseCode("WH-TEST-001");
        warehouse.setWarehouseName("Test Warehouse");
        warehouse.setLength(new BigDecimal("100.5"));
        warehouse.setWidth(new BigDecimal("200.5"));
        warehouse.setIsActive("1");
        warehouse.setCreateTime(new Date());

        // Test POST (Create)
        String json = objectMapper.writeValueAsString(warehouse);
        mockMvc.perform(post("/api/warehouse")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk());

        // Test POST with specific Date Format (simulating potential JSON parse error)
        String rawJson = "{\"warehouseCode\":\"WH-TEST-002\",\"warehouseName\":\"Test Warehouse 2\",\"isActive\":\"1\",\"createTime\":\"2023-10-27 12:00:00\"}";
        mockMvc.perform(post("/api/warehouse")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(rawJson))
                .andDo(print())
                .andExpect(status().isOk());

        // Test GET List
        MvcResult result = mockMvc.perform(get("/api/warehouse/list")
                .session(session)
                .param("warehouseName", "Test"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        
        String content = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(content);
        JsonNode dataNode = rootNode.path("data");
        if (dataNode.isArray() && dataNode.size() > 0) {
            JsonNode firstWarehouse = dataNode.get(0);
            Long id = firstWarehouse.get("id").asLong();
            
            // Test PUT (Update)
            warehouse.setId(id);
            warehouse.setWarehouseName("Updated Warehouse");
            String updateJson = objectMapper.writeValueAsString(warehouse);
            mockMvc.perform(put("/api/warehouse")
                    .session(session)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updateJson))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }

    @Test
    public void testInboundProductionDateShortFormat() throws Exception {
        String json = "{" +
                "\"inbound\": {" +
                "\"warehouseId\": 1," +
                "\"customerId\": 1," +
                "\"inboundType\": \"PO_INBOUND\"," +
                "\"estimatedArrivalTime\": \"2026-01-01\"" +
                "}," +
                "\"inboundDetails\": [" +
                "{" +
                "\"skuId\": 1," +
                "\"batchNo\": \"B1\"," +
                "\"toReceivedQty\": 1," +
                "\"storageUnit\": \"1\"," +
                "\"productionDate\": \"2025-12-02\"," +
                "\"productStatus\": \"0\"," +
                "\"totalWeight\": 0" +
                "}" +
                "]" +
                "}";

        InboundDTO dto = objectMapper.readValue(json, InboundDTO.class);
        assert dto != null;
        assert dto.getInboundDetails() != null && !dto.getInboundDetails().isEmpty();
        assert dto.getInboundDetails().get(0).getProductionDate() != null;
    }
}
