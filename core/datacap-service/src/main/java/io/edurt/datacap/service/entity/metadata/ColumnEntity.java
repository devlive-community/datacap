package io.edurt.datacap.service.entity.metadata;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.service.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "datacap_metadata_column")
@EntityListeners(AuditingEntityListener.class)
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EQ_DOESNT_OVERRIDE_EQUALS"})
public class ColumnEntity
        extends BaseEntity
{
    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private String type;

    @Column(name = "comment")
    private String comment;

    @Column(name = "default_value")
    private String defaultValue;

    @Column(name = "position")
    private String position;

    @Column(name = "is_nullable")
    private String isNullable;

    @Column(name = "maximum_length")
    private String maximumLength;

    @Column(name = "collation")
    private String collation;

    @Column(name = "is_key")
    private String isKey;

    @Column(name = "privileges")
    private String privileges;

    @Column(name = "data_type")
    private String dataType;

    @Column(name = "extra")
    private String extra;

    @ManyToOne
    @JoinTable(name = "datacap_metadata_column_table_relation",
            joinColumns = @JoinColumn(name = "column_id"),
            inverseJoinColumns = @JoinColumn(name = "table_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TableEntity table;
}
