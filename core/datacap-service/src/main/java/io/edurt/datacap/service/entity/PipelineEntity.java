package io.edurt.datacap.service.entity;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.executor.common.RunState;
import io.edurt.datacap.service.body.PipelineBody;
import io.edurt.datacap.service.body.PipelineFieldBody;
import io.edurt.datacap.service.converter.PropertiesConverter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.util.Properties;

@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "datacap_pipeline")
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC"},
        justification = "I prefer to suppress these FindBugs warnings")
public class PipelineEntity
        extends BaseEntity
{
    @Column(name = "content", unique = true)
    private String content;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private RunState state;

    @Column(name = "message")
    private String message;

    @Column(name = "work")
    private String work;

    @Column(name = "elapsed")
    private Long elapsed;

    @Column(name = "executor")
    private String executor = "Seatunnel";

    @ManyToOne
    @JoinColumn(name = "from_id")
    @JsonIncludeProperties(value = {"id", "name", "type"})
    private SourceEntity from;

    @Column(name = "from_configures")
    @Convert(converter = PropertiesConverter.class)
    private Properties fromConfigures;

    @ManyToOne
    @JoinColumn(name = "to_id")
    @JsonIncludeProperties(value = {"id", "name", "type"})
    private SourceEntity to;

    @Column(name = "to_configures")
    @Convert(converter = PropertiesConverter.class)
    private Properties toConfigures;

    @Column(name = "flow_configure")
    private String flowConfigure;

    @ManyToOne
    @JoinTable(name = "datacap_pipeline_user_relation",
            joinColumns = @JoinColumn(name = "pipeline_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private UserEntity user;

    /**
     * Converts a PipelineEntity object to a PipelineBody object.
     *
     * @return the converted PipelineBody object
     */
    public PipelineBody entityToBody()
    {
        PipelineFieldBody from = PipelineFieldBody.builder()
                .id(this.getFrom().getId())
                .configures(this.getFromConfigures())
                .build();
        PipelineFieldBody to = PipelineFieldBody.builder()
                .id(this.getTo().getId())
                .configures(this.getToConfigures())
                .build();
        return PipelineBody.builder()
                .id(this.getId())
                .content(this.getContent())
                .from(from)
                .to(to)
                .executor(this.getExecutor())
                .build();
    }
}
