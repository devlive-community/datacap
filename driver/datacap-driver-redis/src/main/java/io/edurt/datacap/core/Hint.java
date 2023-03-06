package io.edurt.datacap.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hint
{
    /**
     * hint key
     */
    private HintKey key;
    /**
     * hint value
     */
    private String value;
}
