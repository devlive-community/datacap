package io.edurt.datacap.service.entity.metadata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.service.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "datacap_metadata_table")
@EntityListeners(AuditingEntityListener.class)
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EQ_DOESNT_OVERRIDE_EQUALS"})
public class TableEntity
        extends BaseEntity
{
    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private String type;

    @Column(name = "engine")
    private String engine;

    @Column(name = "format")
    private String format;

    @Column(name = "in_rows")
    private String rows;

    @Column(name = "in_create_time")
    private String inCreateTime;

    @Column(name = "in_update_time")
    private String inUpdateTime;

    @Column(name = "collation")
    private String collation;

    @Column(name = "comment")
    private String comment;

    @Column(name = "avg_row_length")
    private String avgRowLength;

    @Column(name = "data_length")
    private String dataLength;

    @Column(name = "index_length")
    private String indexLength;

    @Column(name = "auto_increment")
    private String autoIncrement;

    @ManyToOne()
    @JoinTable(name = "datacap_metadata_table_database_relation",
            joinColumns = @JoinColumn(name = "table_id"),
            inverseJoinColumns = @JoinColumn(name = "database_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DatabaseEntity database;

    @OneToMany(mappedBy = "table", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<ColumnEntity> columns;

    public TableEntity(Long id, String name, Date createTime)
    {
        super(id, name, true, createTime, null);
    }

    public String getRows()
    {
        if (isStringNull(rows)) {
            return "0";
        }
        return rows;
    }

    private boolean isStringNull(String cs)
    {
        return "null".equals(cs);
    }
}
