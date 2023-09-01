package io.edurt.datacap.service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.service.converter.PropertiesConverter;
import io.edurt.datacap.spi.executor.PipelineState;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.sql.Timestamp;
import java.util.Properties;

@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "datacap_pipeline")
@SuppressFBWarnings(value = {"EI_EXPOSE_REP"},
        justification = "I prefer to suppress these FindBugs warnings")
public class PipelineEntity
{
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "content", unique = true)
    private String content;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private PipelineState state;

    @Column(name = "message")
    private String message;

    @Column(name = "work")
    private String work;

    @Column(name = "start_time", columnDefinition = "datetime default CURRENT_TIMESTAMP()")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp startTime;

    @Column(name = "end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp endTime;

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

    @ManyToOne
    @JoinTable(name = "pipeline_user_relation",
            joinColumns = @JoinColumn(name = "pipeline_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIncludeProperties(value = {"id", "username"})
    private UserEntity user;

    public long getElapsed()
    {
        if (ObjectUtils.isNotEmpty(getEndTime())) {
            elapsed = (getEndTime().getTime() - getStartTime().getTime()) / 1000;
        }
        else {
            elapsed = 0L;
        }
        return elapsed;
    }
}
