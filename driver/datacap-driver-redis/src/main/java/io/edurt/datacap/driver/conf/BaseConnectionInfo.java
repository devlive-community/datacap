package io.edurt.datacap.driver.conf;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Properties;

@Data
@NoArgsConstructor
public class BaseConnectionInfo {
    protected String username;
    protected String password;
    protected boolean ssl;
    protected int timeout;

    public BaseConnectionInfo(Properties info) {
        String username = info.getProperty("user");
        String password = info.getProperty("password");
        String sslString = info.getProperty("ssl");
        Object timeoutString = info.getOrDefault("timeout", "1000");

        boolean ssl = "on".equalsIgnoreCase(sslString) || "true".equalsIgnoreCase(sslString);

        this.username = username;
        this.password = password;
        this.timeout = Integer.parseInt(timeoutString.toString());
        this.ssl = ssl;
    }
}
