package io.edurt.datacap.server.entity.admin;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.server.common.MenuEnum;
import io.edurt.datacap.server.validation.ValidationGroup;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "menus")
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE"},
        justification = "I prefer to suppress these FindBugs warnings")
public class MenuEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = {ValidationGroup.Crud.Update.class},
            message = "The passed source id cannot be empty")
    private Long id;

    @NotNull(groups = {ValidationGroup.Crud.Create.class},
            message = "The passed name cannot be empty")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "url")
    private String url;

    @Column(name = "group_name")
    private String group;

    @Column(name = "sorted")
    private int sorted;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private MenuEnum type;

    @Column(name = "parent")
    private Long parent;

    @Column(name = "active")
    private boolean active;

    @Column(name = "create_time")
    @CreatedDate
    private Timestamp createTime;

    @Column(name = "update_time")
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
