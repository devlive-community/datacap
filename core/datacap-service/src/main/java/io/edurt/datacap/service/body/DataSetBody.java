package io.edurt.datacap.service.body;

import io.edurt.datacap.service.entity.DataSetColumnEntity;
import io.edurt.datacap.service.entity.SourceEntity;
import io.edurt.datacap.service.enums.SyncMode;
import lombok.Data;

import java.util.Set;

@Data
public class DataSetBody
{
    private String name;
    private String description;
    private String query;
    private SyncMode syncMode;
    private String syncValue;
    private SourceEntity source;
    private Set<DataSetColumnEntity> columns;
}
