package io.edurt.datacap.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.service.enums.SyncMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.Set;

@Data
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
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

    @Column(name = "sync_value")
    private String syncValue; // only for TIMING

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

    @OneToMany(mappedBy = "dataset", cascade = CascadeType.REMOVE)
    private Set<DataSetColumnEntity> columns;
}
