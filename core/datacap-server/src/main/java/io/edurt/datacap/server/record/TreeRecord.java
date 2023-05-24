package io.edurt.datacap.server.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.edurt.datacap.server.entity.admin.MenuEntity;
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
    private Boolean checked = false;
    private Boolean selected = false;

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
