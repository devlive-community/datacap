package io.edurt.datacap.service.body;

import io.edurt.datacap.spi.FormatType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExportBody
{
    private FormatType format;
    private Long count;
}
