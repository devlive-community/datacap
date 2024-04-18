package io.edurt.datacap.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.service.enums.ReportType;
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
@Table(name = "datacap_report")
@EntityListeners(AuditingEntityListener.class)
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC"})
public class ReportEntity
        extends BaseEntity
{
    @Column(name = "configure")
    private String configure;

    @Column(name = "realtime")
    private boolean realtime;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ReportType type;

    @Column(name = "query")
    private String query;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinTable(name = "datacap_report_user_relation",
            joinColumns = @JoinColumn(name = "report_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnoreProperties(value = {"roles", "thirdConfigure", "avatarConfigure"})
    private UserEntity user;

    @ManyToOne
    @JoinTable(name = "datacap_report_source_relation",
            joinColumns = @JoinColumn(name = "report_id"),
            inverseJoinColumns = @JoinColumn(name = "source_id"))
    @JsonIgnoreProperties(value = {"user"})
    private SourceEntity source;

    @ManyToOne
    @JoinTable(name = "datacap_report_dataset_relation",
            joinColumns = @JoinColumn(name = "report_id"),
            inverseJoinColumns = @JoinColumn(name = "dataset_id"))
    @JsonIgnoreProperties(value = {"user", "columns"})
    private DataSetEntity dataset;
}
