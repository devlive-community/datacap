package io.edurt.datacap.service.body;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.service.entity.DataSetColumnEntity;
import io.edurt.datacap.service.entity.SourceEntity;
import io.edurt.datacap.service.enums.SyncMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class DataSetBody
{
    private Long id;
    private String name;
    private String description;
    private String query;
    private SyncMode syncMode;
    private String syncValue;
    private SourceEntity source;
    private Set<DataSetColumnEntity> columns;
}
