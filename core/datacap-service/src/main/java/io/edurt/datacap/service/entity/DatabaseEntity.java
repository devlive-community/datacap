package io.edurt.datacap.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
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

import java.util.List;

@Data
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "datacap_metadata_database")
@EntityListeners(AuditingEntityListener.class)
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC"})
public class DatabaseEntity
        extends BaseEntity
{
    @Column(name = "description")
    private String description;

    @Column(name = "catalog")
    private String catalog;

    @ManyToOne
    @JoinTable(name = "datacap_metadata_database_source_relation",
            joinColumns = @JoinColumn(name = "source_id"),
            inverseJoinColumns = @JoinColumn(name = "database_id"))
    @JsonIgnore
    private SourceEntity source;

    @OneToMany(mappedBy = "database", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<TableEntity> tables;
}
