package io.edurt.datacap.server.controller;

import io.edurt.datacap.server.BaseParamTest;
import io.edurt.datacap.common.utils.JsonUtils;
import io.edurt.datacap.server.controller.user.FormatController;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(value = SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Slf4j
public class FormatControllerTest
{
    @Autowired
    private FormatController formatController;

    private MockMvc mockMvc;

    @Before
    public void setup()
    {
        mockMvc = MockMvcBuilders.standaloneSetup(formatController).build();
    }

    @Test
    public void formatterSql()
            throws Exception
    {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/format/sql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.objectmapper.writeValueAsString(BaseParamTest.builderFormat())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").exists())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }
}