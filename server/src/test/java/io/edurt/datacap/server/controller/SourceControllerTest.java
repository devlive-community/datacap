package io.edurt.datacap.server.controller;

import io.edurt.datacap.server.BaseParamTest;
import io.edurt.datacap.server.common.JSON;
import io.edurt.datacap.server.entity.SourceEntity;
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
public class SourceControllerTest
{
    @Autowired
    private SourceController sourceController;

    private MockMvc mockMvc;

    @Before
    public void setup()
    {
        mockMvc = MockMvcBuilders.standaloneSetup(sourceController).build();
    }

    @Test
    @Sql(value = "classpath:schema/source.sql")
    public void save()
            throws Exception
    {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/source")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.objectmapper.writeValueAsString(BaseParamTest.builderSource())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    @SqlGroup(value = {
            @Sql(value = "classpath:schema/source.sql"),
            @Sql(value = "classpath:data/source.sql")
    })
    public void update()
            throws Exception
    {
        SourceEntity source = BaseParamTest.builderSource();
        source.setName("TestSource_1");
        source.setId(1L);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/source")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.objectmapper.writeValueAsString(source)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    @SqlGroup(value = {
            @Sql(value = "classpath:schema/source.sql"),
            @Sql(value = "classpath:data/source.sql")
    })
    public void getAll()
            throws Exception
    {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/source")
                        .param("start", "1")
                        .param("end", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    @SqlGroup(value = {
            @Sql(value = "classpath:schema/source.sql"),
            @Sql(value = "classpath:data/source.sql")
    })
    public void delete()
            throws Exception
    {
        Long id = Long.valueOf(1);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/source/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }
}