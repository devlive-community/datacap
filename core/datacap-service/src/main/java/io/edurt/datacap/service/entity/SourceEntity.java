package io.edurt.datacap.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.common.utils.JsonUtils;
import io.edurt.datacap.service.configure.IConfigure;
import io.edurt.datacap.service.validation.ValidationGroup;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "source")
@JsonIgnoreProperties(value = {"configure"})
@org.hibernate.annotations.Table(appliesTo = "source", comment = "The storage is used to query the data connection source")
@SuppressFBWarnings(value = {"EI_EXPOSE_REP"},
        justification = "I prefer to suppress these FindBugs warnings")
public class SourceEntity
{
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = {ValidationGroup.Crud.Update.class}, message = "The passed source id cannot be empty")
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    @NotEmpty(message = "The passed name cannot by empty")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "_type", nullable = false)
    private String type;

    @Column(name = "protocol", unique = true, nullable = false, columnDefinition = "varchar default 'HTTP'")
    @NotNull(message = "The passed protocol cannot by empty")
    private String protocol;

    @Column(name = "host", unique = true, nullable = false)
    @NotEmpty(message = "The passed host cannot by empty")
    private String host;

    @Column(name = "port", unique = true, nullable = false)
    @NotNull(message = "The passed port cannot by empty")
    private Integer port = 0;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "_catalog")
    private String catalog;

    @Column(name = "_database")
    private String database;

    @Column(name = "create_time", columnDefinition = "datetime default CURRENT_TIMESTAMP()")
    private Timestamp createTime;

    // Add from 1.1.0.20221115
    @Column(name = "_ssl", columnDefinition = "boolean default false")
    private Boolean ssl;

    @OneToMany(mappedBy = "plugin", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PluginAuditEntity> pluginAudits;

    // Add from 1.1.0.20221115
    @Column(name = "public", columnDefinition = "boolean default false")
    @JsonProperty(value = "public")
    private Boolean publish; // Public use or not

    @Column(name = "configure")
    @JsonProperty(value = "configure")
    private String configure;

    @Transient
    private Map<String, Object> configures;

    @Transient
    private IConfigure schema;

    @Column(name = "used_config")
    private boolean usedConfig;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIncludeProperties(value = {"id", "username"})
    private UserEntity user;

    public void setConfigure(String configure)
    {
        this.configure = configure;
        if (StringUtils.isNotEmpty(configure)) {
            this.setConfigures(JsonUtils.toMap(this.configure));
        }
    }

    public Map<String, Object> getConfigures()
    {
        if (StringUtils.isNotEmpty(this.configure)) {
            this.setConfigures(JsonUtils.toMap(this.configure));
        }
        return configures;
    }
}
