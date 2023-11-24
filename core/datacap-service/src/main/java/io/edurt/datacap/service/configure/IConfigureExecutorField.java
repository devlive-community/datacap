package io.edurt.datacap.service.configure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class IConfigureExecutorField
{
    // The field name
    private String field;
    // The default is equal to the filed value, and the custom column name uses
    private String origin;
    // When the value is true, it means that the field is required
    private boolean required;
    // If the flag is true
    // it means that the field is extracted through user configuration, and the default data will be discarded
    private boolean override = false;
    // Is it an input parameter
    private boolean input = false;
    // Related component width
    private int width = 300;
    // Component type
    private FieldType type = FieldType.INPUT;
    // User-defined prompts
    private String tooltip;
    // The result of the current configuration input
    private Object value;
    // If this configuration item is true, it will not be displayed on the front end and will be displayed after it is enabled.
    private boolean hidden = false;
    // The description
    private String description;
    // If the type is SELECT , the default data passed in is required
    private List<String> defaultValues;
    // If it is auto-generated, the format is：protocol://host:port
    private boolean generated = false;
}
