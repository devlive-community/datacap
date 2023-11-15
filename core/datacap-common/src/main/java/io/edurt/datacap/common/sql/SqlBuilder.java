package io.edurt.datacap.common.sql;

import com.google.common.base.Preconditions;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.common.sql.builder.AlterBuilder;
import io.edurt.datacap.common.sql.builder.DeleteBuilder;
import io.edurt.datacap.common.sql.builder.SelectBuilder;
import io.edurt.datacap.common.sql.builder.ShowBuilder;
import io.edurt.datacap.common.sql.builder.UpdateBuilder;
import io.edurt.datacap.common.sql.configure.SqlBody;
import io.edurt.datacap.common.sql.configure.SqlColumn;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@SuppressFBWarnings(value = {"DM_STRING_VOID_CTOR"},
        justification = "I prefer to suppress these FindBugs warnings")
public class SqlBuilder
{
    private final SqlBody configure;

    public SqlBuilder(SqlBody configure)
    {
        this.configure = configure;
    }

    public String getSql()
    {
        String sql = new String();
        switch (configure.getType()) {
            case SELECT:
                sql = getSelect();
                break;
            case UPDATE:
                sql = getUpdate();
                break;
            case DELETE:
                sql = getDelete();
                break;
            case ALTER:
                sql = getAlter();
                break;
            case SHOW:
                sql = showCreateTable();
                break;
            default:
                Preconditions.checkArgument(false, "Not support type");
        }
        return sql;
    }

    private String applyDatabaseAndTable()
    {
        Preconditions.checkArgument(StringUtils.isNotEmpty(configure.getTable()), "The table name must be specified");
        if (StringUtils.isNotEmpty(configure.getDatabase())) {
            return String.format("`%s`.`%s`", configure.getDatabase(), configure.getTable());
        }
        return configure.getTable();
    }

    private List<String> applySelectColumns()
    {
        Preconditions.checkArgument(ArrayUtils.isNotEmpty(configure.getColumns().toArray(new SqlColumn[0])), "The columns must be specified");
        return configure.getColumns().stream()
                .map(SqlColumn::getColumn)
                .collect(Collectors.toList());
    }

    private List<String> applyOrderByColumns()
    {
        return configure.getOrders().stream()
                .map(v -> String.join(" ", v.getColumn(), v.getOrder().name()))
                .collect(Collectors.toList());
    }

    private String applyWhere()
    {
        return configure.getWhere()
                .stream()
                .map(v -> String.format("`%s` %s '%s'", v.getColumn(), v.getOperator().getSymbol(), StringEscapeUtils.escapeSql(v.getValue())))
                .collect(Collectors.joining(configure.getCondition()));
    }

    private String getSelect()
    {
        SelectBuilder.BEGIN();
        SelectBuilder.SELECT(applySelectColumns());
        SelectBuilder.FROM(applyDatabaseAndTable());

        if (ObjectUtils.isNotEmpty(configure.getOrders())) {
            SelectBuilder.ORDER_BY(applyOrderByColumns());
        }

        if (configure.getWhere() != null && !configure.getWhere().isEmpty()) {
            SelectBuilder.WHERE(applyWhere());
        }

        if (configure.getLimit() != null) {
            SelectBuilder.LIMIT(configure.getLimit());
        }

        if (configure.getOffset() != null) {
            SelectBuilder.OFFSET(configure.getOffset());
        }
        return SelectBuilder.SQL();
    }

    /**
     * Generates a list of update columns based on the configured columns.
     *
     * @return A list of update columns in the format "`column` = 'value'".
     */
    private List<String> applyUpdateColumns()
    {
        Preconditions.checkArgument(ArrayUtils.isNotEmpty(configure.getColumns().toArray(new SqlColumn[0])), "The columns must be specified");
        return configure.getColumns()
                .stream()
                .map(column -> String.format("`%s` = '%s'", column.getColumn(), StringEscapeUtils.escapeSql(column.getValue())))
                .collect(Collectors.toList());
    }

    public List<String> applyUpdateWhere()
    {
        return configure.getWhere()
                .stream()
                .map(column -> String.format("`%s` %s '%s'", column.getColumn(), column.getOperator().getSymbol(), StringEscapeUtils.escapeSql(column.getValue())))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the update SQL statement based on the specified database and table,
     * update columns, and optional WHERE clause.
     *
     * @return The update SQL statement.
     */
    private String getUpdate()
    {
        UpdateBuilder.BEGIN();
        UpdateBuilder.UPDATE(applyDatabaseAndTable());

        UpdateBuilder.SET(applyUpdateColumns());

        if (configure.getWhere() != null) {
            UpdateBuilder.WHERE(applyUpdateWhere());
        }

        UpdateBuilder.END();
        return UpdateBuilder.SQL();
    }

    /**
     * Generates a SQL DELETE statement based on the provided configuration.
     *
     * @return The generated SQL DELETE statement.
     */
    private String getDelete()
    {
        DeleteBuilder.BEGIN();
        DeleteBuilder.DELETE_FROM(applyDatabaseAndTable());

        if (configure.getWhere() != null) {
            DeleteBuilder.WHERE(applyUpdateWhere());
        }

        DeleteBuilder.END();
        return DeleteBuilder.SQL();
    }

    /**
     * Generate the function comment for the given function body in a markdown code block with the correct language syntax.
     *
     * @return The generated function comment.
     */
    private String getAlter()
    {
        AlterBuilder.BEGIN();
        AlterBuilder.ALTER_TABLE(applyDatabaseAndTable());
        AlterBuilder.AUTO_INCREMENT(configure.getValue());
        return AlterBuilder.SQL();
    }

    /**
     * Generate the function comment for the given function body in a markdown code block with the correct language syntax.
     *
     * @return The generated function comment in markdown format.
     */
    private String showCreateTable()
    {
        ShowBuilder.BEGIN();
        ShowBuilder.SHOW_CREATE_TABLE(applyDatabaseAndTable());
        return ShowBuilder.SQL();
    }
}
