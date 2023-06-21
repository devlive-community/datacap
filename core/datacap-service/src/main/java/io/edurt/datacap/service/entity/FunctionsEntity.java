package io.edurt.datacap.service.entity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.common.enums.FunctionType;
import io.edurt.datacap.service.validation.ValidationGroup;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "functions")
@org.hibernate.annotations.Table(appliesTo = "functions", comment = "Plug-in correlation function")
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE"},
        justification = "I prefer to suppress these FindBugs warnings")
public class FunctionsEntity
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
    @Column(name = "content")
    private String content;

    @NotBlank(groups = {
            ValidationGroup.Crud.Create.class,
            ValidationGroup.Crud.Update.class
    })
    @Column(name = "description")
    private String description;

    @NotNull(groups = {
            ValidationGroup.Crud.Create.class,
            ValidationGroup.Crud.Update.class
    })
    @Size(min = 1)
    @Column(name = "plugin")
    private String plugin;

    @NotBlank(groups = {
            ValidationGroup.Crud.Create.class,
            ValidationGroup.Crud.Update.class
    })
    @Column(name = "example")
    private String example;

    @Column(name = "type", unique = true)
    @Enumerated(EnumType.STRING)
    private FunctionType type = FunctionType.KEYWORD;

    @Column(name = "create_time", columnDefinition = "timestamp not null default current_timestamp")
    private Timestamp createTime;

    @Column(name = "update_time", columnDefinition = "timestamp not null default current_timestamp")
    private Timestamp updateTime;

    public List<String> getPlugin()
    {
        return Arrays.asList(plugin.split(","));
    }

    public void setPlugin(List<String> plugin)
    {
        this.plugin = String.join(",", plugin);
    }
}
