package io.edurt.datacap.server.itransient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ContributionRadar
{
    private Long count;
    private String label;
    private Float percentage;
}
