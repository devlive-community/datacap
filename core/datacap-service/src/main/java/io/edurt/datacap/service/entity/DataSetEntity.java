package io.edurt.datacap.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.common.enums.DataSetState;
import io.edurt.datacap.service.converter.ListConverter;
import io.edurt.datacap.service.enums.SyncMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.List;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "datacap_dataset")
@EntityListeners(AuditingEntityListener.class)
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC"})
public class DataSetEntity
        extends BaseEntity
{
    @Column(name = "description")
    private String description;

    @Column(name = "query")
    private String query;

    @Column(name = "sync_mode")
    @Enumerated(EnumType.STRING)
    private SyncMode syncMode = SyncMode.MANUAL;

    @Column(name = "expression")
    private String expression; // only for TIMING

    @Column(name = "state")
    @Convert(converter = ListConverter.class)
    private List<DataSetState> state;

    @Column(name = "message")
    private String message;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "code")
    private String code;

    @Column(name = "scheduler")
    private String scheduler;

    @Column(name = "executor")
    private String executor;

    @Column(name = "total_rows")
    private String totalRows;

    @Column(name = "total_size")
    private String totalSize;

    @Column(name = "life_cycle")
    private String lifeCycle;

    @Column(name = "life_cycle_column")
    private String lifeCycleColumn;

    @Column(name = "life_cycle_type")
    private String lifeCycleType;

    @ManyToOne
    @JoinTable(name = "datacap_dataset_source_relation",
            joinColumns = @JoinColumn(name = "dataset_id"),
            inverseJoinColumns = @JoinColumn(name = "source_id"))
    @JsonIgnoreProperties(value = {"user"})
    private SourceEntity source;

    @ManyToOne
    @JoinTable(name = "datacap_dataset_user_relation",
            joinColumns = @JoinColumn(name = "dataset_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnoreProperties(value = {"roles", "thirdConfigure", "avatarConfigure"})
    private UserEntity user;

    @Transient
    private Set<DataSetColumnEntity> columns;
}
