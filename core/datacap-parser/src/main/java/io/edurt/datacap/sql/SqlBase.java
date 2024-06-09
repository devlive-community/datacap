package io.edurt.datacap.sql;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REPR", "EI_EXPOSE_REP2"})
public class SqlBase
{
    private String token;
    private String childToken;
    private List<String> columns = new ArrayList<>();
    private String table;
    private boolean successful = false;
    private String message;
}
