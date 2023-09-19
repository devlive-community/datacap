package io.edurt.datacap.service.entity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "datacap_scheduled_history")
@EntityListeners(AuditingEntityListener.class)
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EQ_DOESNT_OVERRIDE_EQUALS"})
public class ScheduledHistoryEntity
        extends BaseEntity
{
    @Column(name = "database_added_count")
    private Long databaseAddedCount;

    @Column(name = "database_removed_count")
    private Long databaseRemovedCount;

    @Column(name = "table_added_count")
    private Long tableAddedCount;

    @Column(name = "table_removed_count")
    private Long tableRemovedCount;

    @Column(name = "column_added_count")
    private Long columnAddedCount;

    @Column(name = "column_removed_count")
    private Long columnRemovedCount;

    @ManyToOne
    @JoinTable(name = "datacap_scheduled_history_relation",
            joinColumns = @JoinColumn(name = "scheduled_id"),
            inverseJoinColumns = @JoinColumn(name = "history_id"))
    private ScheduledEntity scheduled;
}
