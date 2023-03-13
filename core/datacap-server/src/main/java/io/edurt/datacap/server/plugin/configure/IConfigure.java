package io.edurt.datacap.server.plugin.configure;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2", "EI_EXPOSE_REP2", "EI_EXPOSE_REP", "NP_NULL_PARAM_DEREF_NONVIRTUAL"},
        justification = "I prefer to suppress these FindBugs warnings")
public class IConfigure
{
    private String name;
    private Date supportTime;
    private List<IConfigureField> configures;
    private List<IConfigureExecutor> pipelines;
}
