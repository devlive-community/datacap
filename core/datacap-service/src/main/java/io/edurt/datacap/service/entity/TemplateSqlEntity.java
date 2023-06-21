package io.edurt.datacap.service.entity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.service.validation.ValidationGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "template_sql")
@SuppressFBWarnings(value = {"EI_EXPOSE_REP"},
        justification = "I prefer to suppress these FindBugs warnings")
public class TemplateSqlEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = {ValidationGroup.Crud.Update.class}, message = "The passed source id cannot be empty")
    private Long id;

    @NotBlank(groups = {
            ValidationGroup.Crud.Create.class,
            ValidationGroup.Crud.Update.class
    })
    @Column(name = "name")
    private String name;

    @NotBlank(groups = {
            ValidationGroup.Crud.Create.class,
            ValidationGroup.Crud.Update.class
    })
    @Column(name = "description")
    private String description;

    @NotBlank(groups = {
            ValidationGroup.Crud.Create.class,
            ValidationGroup.Crud.Update.class
    })
    @Column(name = "content")
    private String content;

    @NotBlank(groups = {
            ValidationGroup.Crud.Create.class,
            ValidationGroup.Crud.Update.class
    })
    @Column(name = "plugin")
    private String plugin;

    @Column(name = "configure")
    private String configure;

    @Column(name = "system")
    private boolean system;

    @Column(name = "create_time", columnDefinition = "timestamp not null default current_timestamp")
    private Timestamp createTime;

    @Column(name = "update_time", columnDefinition = "timestamp not null default current_timestamp")
    private Timestamp updateTime;

    @PrePersist
    void prePersist()
    {
        createTime = Timestamp.valueOf(LocalDateTime.now());
    }

    @PreUpdate
    void preUpdate()
    {
        updateTime = Timestamp.valueOf(LocalDateTime.now());
    }
}
