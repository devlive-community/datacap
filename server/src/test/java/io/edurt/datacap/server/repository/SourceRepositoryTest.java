package io.edurt.datacap.server.repository;

import io.edurt.datacap.server.BaseParamTest;
import io.edurt.datacap.server.common.ProtocolEnum;
import io.edurt.datacap.server.entity.SourceEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(value = SpringRunner.class)
@DataJpaTest
public class SourceRepositoryTest
{
    @Autowired
    private SourceRepository sourceRepository;

    @Test
    @Sql(value = "classpath:schema/source.sql")
    public void save()
    {
        SourceEntity source = BaseParamTest.builderSource();
        this.sourceRepository.save(source);
        assertThat(source.getId() > 0);
    }
}
