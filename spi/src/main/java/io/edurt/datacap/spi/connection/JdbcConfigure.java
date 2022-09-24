package io.edurt.datacap.spi.connection;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.spi.model.Configure;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
@SuppressFBWarnings(value = {"EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC"},
        justification = "I prefer to suppress these FindBugs warnings")
public class JdbcConfigure
        extends Configure
{
    private String jdbcDriver;
    private String jdbcType;
}
