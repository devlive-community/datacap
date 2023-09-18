package io.edurt.datacap.service.entity.metadata;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.service.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "datacap_metadata_column")
@EntityListeners(AuditingEntityListener.class)
@SuppressFBWarnings(value = {"EI_EXPOSE_REP"})
public class ColumnEntity
        extends BaseEntity
{
    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private String type;

    @Column(name = "length")
    private Integer length;

    @Column(name = "is_primary")
    private Boolean isPrimary;

    @Column(name = "is_index")
    private Boolean isIndex;

    @ManyToOne
    @JoinTable(name = "datacap_metadata_column_table_relation",
            joinColumns = @JoinColumn(name = "column_id"),
            inverseJoinColumns = @JoinColumn(name = "table_id"))
    private TableEntity table;
}
