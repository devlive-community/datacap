package io.edurt.datacap.lib.shell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ShellResponse
{
    private Integer code;
    private Boolean successful;
    private boolean timeout = false;
    private List<String> errors;
}
