package io.edurt.datacap.spi.model;

import com.google.inject.Injector;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;
import java.util.Optional;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class Configure
{
    private Injector injector;
    private String host;
    private Integer port;
    private Optional<String> username = Optional.empty();
    private Optional<String> password = Optional.empty();
    private Optional<String> database = Optional.empty();
    private Optional<String> version = Optional.empty();
    private Optional<Map<String, Object>> env = Optional.empty();
    private Optional<Boolean> ssl = Optional.empty();
    private String format = "Json";
    // if `to`: skip
    private Optional<String> query = Optional.empty();
    // Support for custom upload configuration plugins
    private String home;
    private boolean usedConfig;
    private String id;
}
