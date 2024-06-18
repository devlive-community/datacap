package io.edurt.datacap.service.entity;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.common.enums.State;
import io.edurt.datacap.service.enums.QueryMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "datacap_source_query")
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC", "EQ_DOESNT_OVERRIDE_EQUALS"},
        justification = "I prefer to suppress these FindBugs warnings")
public class PluginAuditEntity
        extends BaseEntity
{
    @Column(name = "state", unique = true)
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "content", unique = true)
    private String content;

    @Column(name = "message", unique = true)
    private String message;

    @Column(name = "elapsed")
    private Long elapsed;

    @Column(name = "count")
    private int count;

    @Column(name = "query_mode")
    @Enumerated(EnumType.STRING)
    private QueryMode mode;

    @Column(name = "code")
    private String code;

    @Column(name = "home")
    private String home;

    @Column(name = "format")
    private String format;

    @ManyToOne
    @JoinColumn(name = "plugin_id")
    @JsonIncludeProperties(value = {"id", "name", "type", "code"})
    private SourceEntity source;

    // Add from 1.1.0.20221115
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIncludeProperties(value = {"id", "username"})
    private UserEntity user;
}
