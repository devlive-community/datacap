package io.edurt.datacap.server.body;

import io.edurt.datacap.server.common.FunctionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FunctionsImportBody
{
    @NotBlank
    private String content;

    @NotNull
    @Size(min = 1)
    private List<String> plugin;
    private FunctionType type = FunctionType.KEYWORD;
}
