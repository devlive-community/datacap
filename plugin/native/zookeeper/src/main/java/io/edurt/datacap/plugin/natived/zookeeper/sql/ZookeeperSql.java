package io.edurt.datacap.plugin.natived.zookeeper.sql;

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
public class ZookeeperSql
{
    private ZookeeperSqlToken token;
    private boolean from;
    private List<String> columns = new ArrayList<>();
    private String table;
    private boolean isSupport = false;
}
