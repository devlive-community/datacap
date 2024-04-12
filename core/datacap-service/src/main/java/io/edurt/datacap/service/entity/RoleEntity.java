package io.edurt.datacap.service.entity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "datacap_role")
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE"},
        justification = "I prefer to suppress these FindBugs warnings")
public class RoleEntity
        extends BaseEntity
{
    @Column(name = "description", columnDefinition = "varchar(1000)")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "datacap_role_menu_relation",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id"))
    private Set<MenuEntity> menus = new HashSet<>();

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
        return String.format("ROLE_%s", this.getName().toUpperCase());
    }
}
