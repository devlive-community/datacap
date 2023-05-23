package io.edurt.datacap.server.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
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
    private Boolean checked = false;
    private Boolean selected = false;
}
