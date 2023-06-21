package io.edurt.datacap.server.controller;

import io.edurt.datacap.server.BaseParamTest;
import io.edurt.datacap.common.utils.JsonUtils;
import io.edurt.datacap.server.controller.user.ExecuteController;
import io.edurt.datacap.service.entity.ExecuteEntity;
import io.edurt.datacap.spi.FormatType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
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
@SqlGroup(value = {@Sql(value = "classpath:schema/source.sql")})
public class ExecuteControllerTest
{
    @Autowired private ExecuteController executeController;

    private MockMvc mockMvc;

    @Before
    public void setup()
    {
        mockMvc = MockMvcBuilders.standaloneSetup(executeController).build();
    }

    @Test
    public void execute()
            throws Exception
    {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/execute").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.objectmapper.writeValueAsString(BaseParamTest.builderExecute()))).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.code").exists()).andDo(MockMvcResultHandlers.print()).andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void executeFormatJson()
            throws Exception
    {
        ExecuteEntity entity = BaseParamTest.builderExecute();
        entity.setFormat(FormatType.JSON);
        entity.setName("MySQL1");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/execute").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.objectmapper.writeValueAsString(entity))).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.code").exists()).andDo(MockMvcResultHandlers.print()).andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }
}