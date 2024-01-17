package io.edurt.datacap.service.entity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.executor.common.RunState;
import io.edurt.datacap.service.enums.QueryMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "datacap_dataset_history")
@EntityListeners(AuditingEntityListener.class)
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC"})
public class DatasetHistoryEntity
        extends BaseEntity
{
    @Column(name = "query")
    private String query;

    @Column(name = "message", unique = true)
    private String message;

    @Column(name = "elapsed")
    private Long elapsed;

    @Column(name = "count")
    private int count;

    @Column(name = "query_mode")
    @Enumerated(EnumType.STRING)
    private QueryMode mode;

    @Column(name = "state", unique = true)
    @Enumerated(EnumType.STRING)
    private RunState state;

    @ManyToOne
    @JoinTable(name = "datacap_dataset_history_relation",
            joinColumns = @JoinColumn(name = "dataset_history_id"),
            inverseJoinColumns = @JoinColumn(name = "dataset_id"))
    private DataSetEntity dataset;
}
