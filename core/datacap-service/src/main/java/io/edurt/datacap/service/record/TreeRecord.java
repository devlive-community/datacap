package io.edurt.datacap.service.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.service.entity.MenuEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class TreeRecord
{
    List<TreeRecord> children;
    private Long id;
    private String title;
    private String url;
    private Integer sorted;
    private String code;
    private String i18nKey;
    private String icon;
    private boolean isNew;
    private Boolean checked = false;
    private Boolean selected = false;
    private String description;

    public static TreeRecord of(MenuEntity entity, boolean checked, boolean selected, List<TreeRecord> children)
    {
        return TreeRecord.builder()
                .id(entity.getId())
                .title(entity.getName())
                .url(entity.getUrl())
                .sorted(entity.getSorted())
                .code(entity.getCode())
                .i18nKey(entity.getI18nKey())
                .icon(entity.getIcon())
                .checked(checked)
                .selected(selected)
                .children(children)
                .build();
    }
}
