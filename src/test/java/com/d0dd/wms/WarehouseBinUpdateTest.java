package com.d0dd.wms;

import com.d0dd.wms.entity.WarehouseBin;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WarehouseBinUpdateTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testUpdateNonExistentBin() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();
        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession();

        WarehouseBin bin = new WarehouseBin();
        bin.setId(99999L); // Non-existent ID
        bin.setBinName("Non-existent Bin");
        bin.setWarehouseId(1L);

        String json = objectMapper.writeValueAsString(bin);

        // Before fix: This expects 200 OK because the bug exists
        // After fix: This should expect an error status (e.g. 500)
        mockMvc.perform(put("/api/warehouse/bin")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk()) // Global Exception Handler returns HTTP 200 with code 500 in body
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("更新失败：ID不存在"));
    }
}
