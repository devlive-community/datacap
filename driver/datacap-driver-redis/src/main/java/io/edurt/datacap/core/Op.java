package io.edurt.datacap.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Op {
    /**
     * 执行的原始SQL
     */
    private String originSql;

    /**
     * SQL中解析出来的hint
     */
    private List<Hint> hints;

    /**
     * SQL中解析出来的command
     */
    private String command;

    /**
     * SQL解析出来的参数
     */
    private String[] params;
}
