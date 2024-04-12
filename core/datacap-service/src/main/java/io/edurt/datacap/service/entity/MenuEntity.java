package io.edurt.datacap.service.entity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.common.enums.MenuEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "datacap_menu")
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE"},
        justification = "I prefer to suppress these FindBugs warnings")
public class MenuEntity
        extends BaseEntity
{
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
    private long parent;

    @Column(name = "i18n_key")
    private String i18nKey;

    @Column(name = "icon")
    private String icon;

    @Column(name = "redirect")
    private long redirect;

    @Column(name = "is_new")
    private boolean isNew;
}
