-- --------------------------------
--       Update to 1.11.0        --
-- --------------------------------
alter table `audit_plugin`
    add column `count` bigint(20) default 0;

update `menus`
set url = '/admin'
where name = '全局 - 管理主菜单';

update `menus`
set url = '/system'
where name = '管理员 - 系统主菜单';

update `menus`
set url = '/system/function'
where url = '/admin/function';

update `menus`
set url = '/system/schedule'
where url = '/admin/schedule';

update `menus`
set url = '/system/template'
where url = '/admin/template';

update `menus`
set url = '/system/role'
where url = '/admin/role';

update `menus`
set url = '/system/menu'
where url = '/admin/menu';

update `menus`
set url = '/system/user'
where url = '/admin/user';

INSERT INTO `template_sql` (name, content, description, plugin, configure, `system`)
VALUES ('getAllDatabase', 'SELECT keyspace_name AS name
FROM system_schema.keyspaces', 'Gets a list of all databases', 'Cassandra', '[]', 1);
INSERT INTO `template_sql` (name, content, description, plugin, configure, `system`)
VALUES ('getAllTablesFromDatabase', 'SELECT
  table_name AS name
FROM
  system_schema.tables
WHERE
  keyspace_name = ''${database:String}''
GROUP BY
  table_name', 'Get the data table from the database', 'Cassandra', '[{"column":"database","type":"String","expression":"${database:String}"}]', 1);
INSERT INTO `template_sql` (name, content, description, plugin, configure, `system`)
VALUES ('getAllColumnsFromDatabaseAndTable', 'SELECT
  column_name
FROM
  system_schema.columns
WHERE
  keyspace_name = ''${database:String}''
  and table_name = ''${table:String}''', 'Get the data column from the database and table', 'Cassandra',
        '[{"column":"database","type":"String","expression":"${database:String}"},{"column":"table","type":"String","expression":"${table:String}"}]', 1);
