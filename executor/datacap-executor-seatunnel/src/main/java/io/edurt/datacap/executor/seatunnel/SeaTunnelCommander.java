package io.edurt.datacap.executor.seatunnel;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SeaTunnelCommander
{
    private final String bin;
    private final String startScript;
    private final String master;
    private final String deployMode;
    private final String config;
    private final String applicationName;

    public SeaTunnelCommander(String bin, String config, String applicationName)
    {
        this.bin = bin;
        this.startScript = "start-seatunnel-spark-connector-v2.sh";
        this.master = "local";
        this.deployMode = "client";
        this.config = config;
        this.applicationName = applicationName;
    }

    public SeaTunnelCommander(String bin, String startScript, String master, String deployMode, String config, String applicationName)
    {
        this.bin = bin;
        this.startScript = startScript;
        this.master = master;
        this.deployMode = deployMode;
        this.config = config;
        this.applicationName = applicationName;
    }

    public String toCommand()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.bin);
        buffer.append("/");
        buffer.append(startScript);
        buffer.append(" --master ");
        buffer.append(this.master);
        buffer.append(" --deploy-mode ");
        buffer.append(this.deployMode);
        buffer.append(" --config ");
        buffer.append(this.config);
        buffer.append(" --name ");
        buffer.append(this.applicationName);
        return buffer.toString();
    }
}
