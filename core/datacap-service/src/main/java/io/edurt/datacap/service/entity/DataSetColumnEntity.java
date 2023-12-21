package io.edurt.datacap.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.service.enums.ColumnType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "datacap_dataset_column")
@EntityListeners(AuditingEntityListener.class)
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC"})
public class DataSetColumnEntity
        extends BaseEntity
{
    @Column(name = "description")
    private String description;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ColumnType type;

    @Column(name = "comment")
    private String comment;

    @Column(name = "default_value")
    private String defaultValue;

    @Column(name = "position")
    private int position;

    @Column(name = "is_nullable")
    private boolean isNullable;

    @Column(name = "length")
    private int length;

    @ManyToOne
    @JoinTable(name = "datacap_dataset_column_relation",
            joinColumns = @JoinColumn(name = "column_id"),
            inverseJoinColumns = @JoinColumn(name = "dataset_id"))
    @JsonIgnore
    private DataSetEntity dataset;
}
