package io.edurt.datacap.service.body.adhoc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Adhoc
{
    private Set<AdhocColumn> columns;
}
