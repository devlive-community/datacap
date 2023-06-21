package io.edurt.datacap.service.common;

import com.google.common.base.Preconditions;
import io.edurt.datacap.common.enums.ServiceState;
import io.edurt.datacap.common.utils.JsonUtils;
import io.edurt.datacap.service.configure.IConfigure;
import io.edurt.datacap.service.configure.IConfigureField;
import io.edurt.datacap.service.configure.IConfigureFieldName;
import io.edurt.datacap.service.configure.IConfigureFieldType;
import io.edurt.datacap.service.entity.SourceEntity;
import io.edurt.datacap.spi.FormatType;
import io.edurt.datacap.spi.model.Configure;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ConfigureUtils
{
    private ConfigureUtils()
    {}

    public static String preparedMessage(List<IConfigureField> configures)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(ServiceState.PLUGIN_CONFIGURE_REQUIRED.getValue());
        configures.forEach(v -> {
            buffer.append("<br/>");
            buffer.append("Field: " + v.getField());
        });
        return buffer.toString();
    }

    public static Configure preparedConfigure(List<IConfigureField> configures)
    {
        Configure configure = new Configure();
        configures.forEach(v -> {
            switch (v.getField()) {
                case host:
                    configure.setHost(ConfigureUtils.getStringValue(configures, IConfigureFieldName.host));
                    break;
                case port:
                    configure.setPort(ConfigureUtils.getIntegerValue(configures, IConfigureFieldName.port));
                    break;
                case username:
                    configure.setUsername(Optional.ofNullable(ConfigureUtils.getStringValue(configures, IConfigureFieldName.username)));
                    break;
                case password:
                    configure.setPassword(Optional.ofNullable(ConfigureUtils.getStringValue(configures, IConfigureFieldName.password)));
                    break;
                case database:
                    String database = ConfigureUtils.getStringValue(configures, IConfigureFieldName.database);
                    Optional<String> _database = StringUtils.isNotEmpty(database) ? Optional.ofNullable(database) : Optional.empty();
                    configure.setDatabase(_database);
                    break;
                case ssl:
                    configure.setSsl(Optional.ofNullable(ConfigureUtils.getBooleanValue(configures, IConfigureFieldName.ssl)));
                    break;
                case configures:
                    configure.setEnv(Optional.ofNullable(ConfigureUtils.getMapValue(configures, IConfigureFieldName.configures)));
                    break;
                case file:
                    configure.setUsedConfig(true);
                    break;
            }
        });
        configure.setFormat(FormatType.JSON);
        return configure;
    }

    public static IConfigure preparedConfigure(IConfigure configure, SourceEntity source)
    {
        configure.getConfigures().forEach(v -> {
            switch (v.getField()) {
                case name:
                    v.setValue(source.getName());
                    break;
                case host:
                    v.setValue(source.getHost());
                    break;
                case port:
                    v.setValue(source.getPort());
                    break;
                case username:
                    v.setValue(source.getUsername());
                    break;
                case password:
                    v.setValue(source.getPassword());
                    break;
                case database:
                    v.setValue(source.getDatabase());
                    break;
                case ssl:
                    v.setValue(source.getSsl());
                    break;
                case catalog:
                    v.setValue(source.getCatalog());
                    break;
                case configures:
                    List<Map<String, Object>> fields = new ArrayList<>();
                    if (ObjectUtils.isNotEmpty(source.getConfigures())) {
                        source.getConfigures().entrySet().forEach(entry -> {
                            Map<String, Object> map = new LinkedHashMap<>();
                            map.put(IConfigureFieldName.field.name(), entry.getKey());
                            map.put(IConfigureFieldName.value.name(), entry.getValue());
                            fields.add(map);
                        });
                    }
                    v.setValue(fields);
                    break;
            }
        });
        return configure;
    }

    public static SourceEntity preparedSourceEntity(List<IConfigureField> configures)
    {
        SourceEntity configure = new SourceEntity();
        configures.forEach(v -> {
            switch (v.getField()) {
                case name:
                    configure.setName(ConfigureUtils.getStringValue(configures, IConfigureFieldName.name));
                case host:
                    configure.setHost(ConfigureUtils.getStringValue(configures, IConfigureFieldName.host));
                    break;
                case port:
                    configure.setPort(ConfigureUtils.getIntegerValue(configures, IConfigureFieldName.port));
                    break;
                case username:
                    configure.setUsername(ConfigureUtils.getStringValue(configures, IConfigureFieldName.username));
                    break;
                case password:
                    configure.setPassword(ConfigureUtils.getStringValue(configures, IConfigureFieldName.password));
                    break;
                case database:
                    String database = ConfigureUtils.getStringValue(configures, IConfigureFieldName.database);
                    configure.setDatabase(database);
                    break;
                case catalog:
                    String catalog = ConfigureUtils.getStringValue(configures, IConfigureFieldName.catalog);
                    configure.setCatalog(catalog);
                    break;
                case ssl:
                    configure.setSsl(ConfigureUtils.getBooleanValue(configures, IConfigureFieldName.ssl));
                    break;
                case configures:
                    configure.setConfigure(JsonUtils.toJSON(ConfigureUtils.getMapValue(configures, IConfigureFieldName.configures)));
                    break;
                case file:
                    configure.setUsedConfig(true);
                    break;
            }
        });
        configure.setCreateTime(Timestamp.from(Instant.now()));
        return configure;
    }

    public static List<IConfigureField> filterNotEmpty(List<IConfigureField> configures)
    {
        return configures.stream().filter(v -> !isEmpty(v)).collect(Collectors.toList());
    }

    public static boolean isEmpty(IConfigureField field)
    {
        boolean flag = true;
        switch (field.getType()) {
            case String:
                if (ObjectUtils.isNotEmpty(field.getValue())) {
                    flag = StringUtils.isEmpty(String.valueOf(field.getValue()));
                }
                break;
            default:
                flag = false;
        }
        return flag;
    }

    public static IConfigureField getConfigure(List<IConfigureField> configures, IConfigureFieldName key)
    {
        Optional<IConfigureField> configureFieldOptional = configures.stream().filter(v -> v.getField().equals(key)).findFirst();
        return configureFieldOptional.get();
    }

    public static String getStringValue(List<IConfigureField> configures, IConfigureFieldName key)
    {
        return String.valueOf(getConfigure(configures, key).getValue());
    }

    public static Integer getIntegerValue(List<IConfigureField> configures, IConfigureFieldName key)
    {
        return Integer.valueOf(getStringValue(configures, key));
    }

    public static Long getLongValue(List<IConfigureField> configures, IConfigureFieldName key)
    {
        return Long.valueOf(getStringValue(configures, key));
    }

    public static Boolean getBooleanValue(List<IConfigureField> configures, IConfigureFieldName key)
    {
        return Boolean.valueOf(getStringValue(configures, key));
    }

    public static Map<String, Object> getMapValue(List<IConfigureField> configures, IConfigureFieldName key)
    {
        Map<String, Object> values = new ConcurrentHashMap<>();
        IConfigureField configureField = getConfigure(configures, key);
        if (configureField.getType().equals(IConfigureFieldType.Array)) {
            List<LinkedHashMap<String, Object>> list = (List<LinkedHashMap<String, Object>>) configureField.getValue();
            list.forEach(map -> values.put(String.valueOf(map.get(IConfigureFieldName.field.name())), map.get(IConfigureFieldName.value.name())));
        }
        else {
            Preconditions.checkArgument(false, "Not Support type");
        }
        return values;
    }
}
