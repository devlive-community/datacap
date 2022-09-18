package io.edurt.datacap.server.entity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.server.common.ProtocolEnum;
import io.edurt.datacap.server.validation.ValidationGroup;
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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "source")
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

    @Column(name = "protocol", unique = true, nullable = false, columnDefinition = "varchar default 'HTTP'")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "The passed protocol cannot by empty")
    private ProtocolEnum protocol;

    @Column(name = "host", unique = true, nullable = false)
    @NotEmpty(message = "The passed host cannot by empty")
    private String host;

    @Column(name = "port", unique = true, nullable = false)
    @NotNull(message = "The passed port cannot by empty")
    private Integer port;

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
}
