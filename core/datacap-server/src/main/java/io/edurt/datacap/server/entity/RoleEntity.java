package io.edurt.datacap.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
@org.hibernate.annotations.Table(appliesTo = "role", comment = "User rights configuration table")
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE"},
        justification = "I prefer to suppress these FindBugs warnings")
public class RoleEntity
{
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "varchar(1000)")
    private String description;

    @Column(name = "create_time", columnDefinition = "datetime(5) default CURRENT_TIMESTAMP()")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;

    @Transient
    private boolean isDefault;

    @Transient
    private String code;

    public boolean isDefault()
    {
        return this.getCode().equals("ROLE_ADMIN") || this.getCode().equals("ROLE_USER") ? true : false;
    }

    public String getCode()
    {
        return String.format("ROLE_%s", this.name.toUpperCase());
    }
}
