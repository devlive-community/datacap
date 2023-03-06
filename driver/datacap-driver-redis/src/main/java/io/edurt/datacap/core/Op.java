package io.edurt.datacap.core;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
        justification = "I prefer to suppress these FindBugs warnings")
public class Op
{
    /**
     * Executed original SQL
     */
    private String originSql;

    /**
     * Hint parsed from SQL
     */
    private List<Hint> hints;

    /**
     * COMMAND parsed from SQL
     */
    private String command;

    /**
     * Parameters parsed by SQL
     */
    private String[] params;
}
