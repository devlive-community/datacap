-- --------------------------------
--       Update to 1.10.0        --
-- --------------------------------
/* H2 */
INSERT INTO template_sql(name, content, description, plugin, configure, `system`)
VALUES ('FindTableTypeByDatabase', 'SELECT
  CASE
    INFORMATION_SCHEMA.TABLES.TABLE_TYPE
    WHEN ''VIEW'' THEN ''view''
    ELSE ''table''
  END AS table_type
FROM
  INFORMATION_SCHEMA.TABLES
WHERE
  INFORMATION_SCHEMA.TABLES.TABLE_SCHEMA = ''${database:String}''
GROUP BY
  table_type', 'Finds all table types under the database according to the database', 'H2', '[{"column":"database","type":"String","expression":"${database:String}"}]', TRUE),
       ('FindTableByDatabaseAndType', 'SELECT
  TABLE_CATALOG,
  TABLE_NAME
FROM
  (
    SELECT
      TABLE_SCHEMA AS TABLE_CATALOG,
      TABLE_NAME AS TABLE_NAME
    FROM
      INFORMATION_SCHEMA.TABLES
    WHERE
      INFORMATION_SCHEMA.TABLES.TABLE_SCHEMA = ''${database:String}''
      AND CASE
        INFORMATION_SCHEMA.TABLES.TABLE_TYPE
        WHEN ''VIEW'' THEN ''view''
        ELSE ''table''
      END = ''${type:String}''
    GROUP BY
      TABLE_NAME,
      TABLE_SCHEMA
  )', 'Gets a collection of related data based on the specified database and data type', 'H2',
        '[{"column":"database","type":"String","expression":"${database:String}"},{"column":"type","type":"String","expression":"${type:String}"}]', TRUE),
       ('FindColumnTypeByDatabaseAndTable', 'SELECT
  ''${database:String}'' AS TABLE_CATALOG,
  COLUMN_TYPE
FROM
  (
    SELECT
      CASE
        WHEN (
          icl.IS_INDEX = ''Y''
          AND col.IS_IDENTITY = ''YES''
        ) THEN ''index,primaryKey''
        WHEN col.IS_IDENTITY = ''YES'' THEN ''primaryKey''
        WHEN icl.IS_INDEX = ''Y'' THEN ''index''
        ELSE ''column''
      END AS COLUMN_TYPE
    FROM
      INFORMATION_SCHEMA.COLUMNS col
      LEFT JOIN (
        SELECT
          TABLE_SCHEMA,
          TABLE_NAME,
          COLUMN_NAME,
          ''Y'' AS IS_KEY
        FROM
          INFORMATION_SCHEMA.KEY_COLUMN_USAGE
      ) kcu ON kcu.TABLE_SCHEMA = col.TABLE_SCHEMA
      AND kcu.TABLE_NAME = col.TABLE_NAME
      AND kcu.COLUMN_NAME = col.COLUMN_NAME
      LEFT JOIN (
        SELECT
          ic.TABLE_SCHEMA AS TABLE_SCHEMA,
          ic.TABLE_NAME AS TABLE_NAME,
          ic.COLUMN_NAME AS COLUMN_NAME,
          ''Y'' AS IS_INDEX
        FROM
          INFORMATION_SCHEMA.INDEXES i,
          INFORMATION_SCHEMA.INDEX_COLUMNS ic
        WHERE
          i.INDEX_NAME = ic.INDEX_NAME
      ) icl ON icl.TABLE_SCHEMA = col.TABLE_SCHEMA
      AND icl.TABLE_NAME = col.TABLE_NAME
      AND icl.COLUMN_NAME = col.COLUMN_NAME
    WHERE
      col.TABLE_SCHEMA = ''${database:String}''
      AND col.TABLE_NAME = ''${table:String}''
    ORDER BY
      col.COLUMN_NAME
  )
GROUP BY
  COLUMN_TYPE', 'Gets the data column classification collection based on the provided database and data table', 'H2',
        '[{"column":"database","type":"String","expression":"${database:String}"},{"column":"table","type":"String","expression":"${table:String}"}]', TRUE),
       ('FindColumnByDatabaseAndTableAndType', 'SELECT
  ''${database:String}'' AS TABLE_CATALOG,
  ''${table:String}'' AS TABLE_NAME,
  COLUMN_NAME,
  COLUMN_TYPE,
  DATA_TYPE
FROM
  (
    SELECT
      col.COLUMN_NAME AS COLUMN_NAME,
      col.DATA_TYPE AS DATA_TYPE,
      CASE
        WHEN (
          icl.IS_INDEX = ''Y''
          AND col.IS_IDENTITY = ''YES''
        ) THEN ''index,primaryKey''
        WHEN col.IS_IDENTITY = ''YES'' THEN ''primaryKey''
        WHEN icl.IS_INDEX = ''Y'' THEN ''index''
        ELSE ''column''
      END AS COLUMN_TYPE
    FROM
      INFORMATION_SCHEMA.COLUMNS col
      LEFT JOIN (
        SELECT
          TABLE_SCHEMA,
          TABLE_NAME,
          COLUMN_NAME,
          ''Y'' AS IS_KEY
        FROM
          INFORMATION_SCHEMA.KEY_COLUMN_USAGE
      ) kcu ON kcu.TABLE_SCHEMA = col.TABLE_SCHEMA
      AND kcu.TABLE_NAME = col.TABLE_NAME
      AND kcu.COLUMN_NAME = col.COLUMN_NAME
      LEFT JOIN (
        SELECT
          ic.TABLE_SCHEMA AS TABLE_SCHEMA,
          ic.TABLE_NAME AS TABLE_NAME,
          ic.COLUMN_NAME AS COLUMN_NAME,
          ''Y'' AS IS_INDEX
        FROM
          INFORMATION_SCHEMA.INDEXES i,
          INFORMATION_SCHEMA.INDEX_COLUMNS ic
        WHERE
          i.INDEX_NAME = ic.INDEX_NAME
      ) icl ON icl.TABLE_SCHEMA = col.TABLE_SCHEMA
      AND icl.TABLE_NAME = col.TABLE_NAME
      AND icl.COLUMN_NAME = col.COLUMN_NAME
    WHERE
      col.TABLE_SCHEMA = ''${database:String}''
      AND col.TABLE_NAME = ''${table:String}''
    ORDER BY
      col.COLUMN_NAME
  )
WHERE
  COLUMN_TYPE LIKE ''%${type:String}%''
GROUP BY
  COLUMN_NAME
ORDER BY
  COLUMN_NAME', 'Gets a collection of related data based on the specified database, table, and data type', 'H2',
        '[{"column":"database","type":"String","expression":"${database:String}"},{"column":"table","type":"String","expression":"${table:String}"},{"column":"type","type":"String","expression":"${type:String}"}]',
        TRUE);
/* MySQL */
INSERT INTO template_sql(name, content, description, plugin, configure, `system`)
VALUES ('FindTableTypeByDatabase', 'SELECT
  CASE
    INFORMATION_SCHEMA.TABLES.TABLE_TYPE
    WHEN ''VIEW'' THEN ''view''
    ELSE ''table''
  END AS table_type
FROM
  INFORMATION_SCHEMA.TABLES
WHERE
  INFORMATION_SCHEMA.TABLES.TABLE_SCHEMA = ''${database:String}''
GROUP BY
  table_type', 'Finds all table types under the database according to the database', 'MySQL', '[{"column":"database","type":"String","expression":"${database:String}"}]', TRUE),
       ('FindTableByDatabaseAndType', 'SELECT
  TABLE_SCHEMA AS TABLE_CATALOG,
  TABLE_NAME AS TABLE_NAME
FROM
  INFORMATION_SCHEMA.TABLES
WHERE
  INFORMATION_SCHEMA.TABLES.TABLE_SCHEMA = ''${database:String}''
  AND CASE
    INFORMATION_SCHEMA.TABLES.TABLE_TYPE
    WHEN ''VIEW'' THEN ''view''
    ELSE ''table''
  END = ''${type:String}''
GROUP BY
  TABLE_NAME,
  TABLE_SCHEMA', 'Gets a collection of related data based on the specified database and data type', 'MySQL',
        '[{"column":"database","type":"String","expression":"${database:String}"},{"column":"type","type":"String","expression":"${type:String}"}]', TRUE),
       ('FindColumnTypeByDatabaseAndTable', 'SELECT
  ''${database:String}'' AS TABLE_CATALOG,
  COLUMN_TYPE
FROM
  (
    SELECT
      CASE
        WHEN (
          icl.IS_INDEX = ''Y''
          AND col.COLUMN_KEY = ''PRI''
        ) THEN ''index,primaryKey''
        WHEN col.COLUMN_KEY = ''PRI'' THEN ''primaryKey''
        WHEN icl.IS_INDEX = ''Y'' THEN ''index''
        ELSE ''column''
      END AS COLUMN_TYPE
    FROM
      INFORMATION_SCHEMA.COLUMNS col
      LEFT JOIN (
        SELECT
          TABLE_SCHEMA,
          TABLE_NAME,
          COLUMN_NAME,
          ''Y'' AS IS_KEY
        FROM
          INFORMATION_SCHEMA.KEY_COLUMN_USAGE
      ) kcu ON kcu.TABLE_SCHEMA = col.TABLE_SCHEMA
      AND kcu.TABLE_NAME = col.TABLE_NAME
      AND kcu.COLUMN_NAME = col.COLUMN_NAME
      LEFT JOIN (
        SELECT
          TABLE_SCHEMA AS TABLE_SCHEMA,
          TABLE_NAME AS TABLE_NAME,
          COLUMN_NAME AS COLUMN_NAME,
          ''Y'' AS IS_INDEX
        FROM
          INFORMATION_SCHEMA.`STATISTICS`
        WHERE
          TABLE_CATALOG = ''${database:String}''
          AND TABLE_NAME = ''${table:String}''
      ) icl ON icl.TABLE_SCHEMA = col.TABLE_SCHEMA
      AND icl.TABLE_NAME = col.TABLE_NAME
      AND icl.COLUMN_NAME = col.COLUMN_NAME
    WHERE
      col.TABLE_SCHEMA = ''${database:String}''
      AND col.TABLE_NAME = ''${table:String}''
    ORDER BY
      col.COLUMN_NAME
  ) AS tmp
GROUP BY
  COLUMN_TYPE', 'Gets the data column classification collection based on the provided database and data table', 'MySQL',
        '[{"column":"database","type":"String","expression":"${database:String}"},{"column":"table","type":"String","expression":"${table:String}"}]', TRUE),
       ('FindColumnByDatabaseAndTableAndType', 'SELECT
  ''${database:String}'' AS TABLE_CATALOG,
  ''${table:String}'' AS TABLE_NAME,
  COLUMN_NAME,
  COLUMN_TYPE,
  DATA_TYPE
FROM
  (
    SELECT
      col.COLUMN_NAME AS COLUMN_NAME,
      col.DATA_TYPE AS DATA_TYPE,
      CASE
        WHEN (
          icl.IS_INDEX = ''Y''
          AND col.COLUMN_KEY = ''PRI''
        ) THEN ''index,primaryKey''
        WHEN col.COLUMN_KEY = ''PRI'' THEN ''primaryKey''
        WHEN icl.IS_INDEX = ''Y'' THEN ''index''
        ELSE ''column''
      END AS COLUMN_TYPE
    FROM
      INFORMATION_SCHEMA.COLUMNS col
      LEFT JOIN (
        SELECT
          TABLE_SCHEMA,
          TABLE_NAME,
          COLUMN_NAME,
          ''Y'' AS IS_KEY
        FROM
          INFORMATION_SCHEMA.KEY_COLUMN_USAGE
      ) kcu ON kcu.TABLE_SCHEMA = col.TABLE_SCHEMA
      AND kcu.TABLE_NAME = col.TABLE_NAME
      AND kcu.COLUMN_NAME = col.COLUMN_NAME
      LEFT JOIN (
        SELECT
          TABLE_SCHEMA AS TABLE_SCHEMA,
          TABLE_NAME AS TABLE_NAME,
          COLUMN_NAME AS COLUMN_NAME,
          ''Y'' AS IS_INDEX
        FROM
          INFORMATION_SCHEMA.`STATISTICS`
        WHERE
          TABLE_CATALOG = ''${database:String}''
          AND TABLE_NAME = ''${table:String}''
      ) icl ON icl.TABLE_SCHEMA = col.TABLE_SCHEMA
      AND icl.TABLE_NAME = col.TABLE_NAME
      AND icl.COLUMN_NAME = col.COLUMN_NAME
    WHERE
      col.TABLE_SCHEMA = ''${database:String}''
      AND col.TABLE_NAME = ''${table:String}''
    ORDER BY
      col.COLUMN_NAME
  ) AS tmp
WHERE
  COLUMN_TYPE LIKE ''%${type:String}%''
GROUP BY
  TABLE_CATALOG,
  TABLE_NAME,
  COLUMN_NAME,
  COLUMN_TYPE,
  DATA_TYPE
ORDER BY
  COLUMN_NAME', 'Gets a collection of related data based on the specified database, table, and data type', 'MySQL',
        '[{"column":"database","type":"String","expression":"${database:String}"},{"column":"table","type":"String","expression":"${table:String}"},{"column":"type","type":"String","expression":"${type:String}"}]',
        TRUE);
/* ClickHouse */
INSERT INTO template_sql(name, content, description, plugin, configure, `system`)
VALUES ('FindTableTypeByDatabase', 'SELECT
  multiIf(
    startsWith(engine, ''System''),
    ''system'',
    endsWith(engine, ''View''),
    ''view'',
    startsWith(engine, ''Kafka''),
    ''kafka'',
    endsWith(engine, ''Log''),
    ''log'',
    ''table''
  ) AS TABLE_TYPE
FROM
  system.tables
WHERE
  database = ''${database:String}''
GROUP BY
  TABLE_TYPE', 'Finds all table types under the database according to the database', 'ClickHouse', '[{"column":"database","type":"String","expression":"${database:String}"}]',
        TRUE),
       ('FindTableByDatabaseAndType', 'SELECT
  `database` AS TABLE_CATALOG,
  name AS TABLE_NAME
FROM
  system.tables
WHERE
  `database` = ''${database:String}''
  AND multiIf(
    startsWith(engine, ''System''),
    ''system'',
    endsWith(engine, ''View''),
    ''view'',
    startsWith(engine, ''Kafka''),
    ''kafka'',
    endsWith(engine, ''Log''),
    ''log'',
    ''table''
  ) = ''${type:String}''
GROUP BY
  TABLE_NAME,
  `database`', 'Gets a collection of related data based on the specified database and data type', 'ClickHouse',
        '[{"column":"database","type":"String","expression":"${database:String}"},{"column":"type","type":"String","expression":"${type:String}"}]', TRUE),
       ('FindColumnTypeByDatabaseAndTable', 'SELECT
  ''${database:String}'' AS TABLE_CATALOG,
  COLUMN_TYPE
FROM
  (
    SELECT
      CASE
        WHEN (
          is_in_primary_key = ''1''
          AND is_in_partition_key = ''1''
        ) THEN ''index,primaryKey''
        WHEN is_in_primary_key = ''1'' THEN ''primaryKey''
        WHEN is_in_primary_key = ''1'' THEN ''index''
        ELSE ''column''
      END AS COLUMN_TYPE
    FROM
      system.columns col
    WHERE
      `database` = ''${database:String}''
      AND `table` = ''${table:String}''
    ORDER BY
      `name`
  ) AS tmp
GROUP BY
  COLUMN_TYPE', 'Gets the data column classification collection based on the provided database and data table', 'ClickHouse',
        '[{"column":"database","type":"String","expression":"${database:String}"},{"column":"table","type":"String","expression":"${table:String}"}]', TRUE),
       ('FindColumnByDatabaseAndTableAndType', 'SELECT
  ''${database:String}'' AS TABLE_CATALOG,
  ''${table:String}'' AS TABLE_NAME,
  COLUMN_NAME,
  COLUMN_TYPE,
  DATA_TYPE
FROM
  (
    SELECT
      `name` AS COLUMN_NAME,
      `type` AS DATA_TYPE,
      CASE
        WHEN (
          is_in_primary_key = ''1''
          AND is_in_partition_key = ''1''
        ) THEN ''index,primaryKey''
        WHEN is_in_primary_key = ''1'' THEN ''primaryKey''
        WHEN is_in_primary_key = ''1'' THEN ''index''
        ELSE ''column''
      END AS COLUMN_TYPE
    FROM
      system.columns col
    WHERE
      `database` = ''${database:String}''
      AND `table` = ''${table:String}''
    ORDER BY
      `name`
  ) AS tmp
WHERE
  COLUMN_TYPE LIKE ''%${type:String}%''
GROUP BY
  TABLE_CATALOG,
  TABLE_NAME,
  COLUMN_NAME,
  COLUMN_TYPE,
  DATA_TYPE
ORDER BY
  COLUMN_NAME', 'Gets a collection of related data based on the specified database, table, and data type', 'ClickHouse',
        '[{"column":"database","type":"String","expression":"${database:String}"},{"column":"table","type":"String","expression":"${table:String}"},{"column":"type","type":"String","expression":"${type:String}"}]',
        TRUE);

CREATE TABLE IF NOT EXISTS `role_menu_relation`
(
    role_id long,
    menu_id long
);

CREATE TABLE IF NOT EXISTS `menus`
(
    `id`          bigint PRIMARY KEY AUTO_INCREMENT,
    `name`        varchar(255) NOT NULL,
    `code`        varchar(255) DEFAULT NULL,
    `description` varchar(255) DEFAULT NULL,
    `url`         varchar(255) NOT NULL,
    `group_name`  varchar(255) DEFAULT NULL,
    `sorted`      int          DEFAULT 0,
    `type`        varchar(10)  DEFAULT 'VIEW',
    `parent`      bigint       DEFAULT 0,
    `active`      boolean      DEFAULT 1,
    `i18n_key`    varchar(255) DEFAULT NULL,
    `icon`        varchar(255) DEFAULT NULL,
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

insert into `menus` (name, code, description, url, group_name, sorted, type, parent, active, i18n_key, icon)
values ('全局 - 首页', 'HOME', '全局路由：所有用户都可以访问', '/dashboard/index', null, 1, 'VIEW', 0, 1, 'common.home', 'ios-navigate'),
       ('全局 - 查询', 'QUERY', '全局路由：所有用户都可以访问', '/console/index', null, 2, 'VIEW', 0, 1, 'common.query', 'md-browsers'),
       ('全局 - 管理主菜单', 'MANAGEMENT', '全局：所有用户都可以访问
位置：顶部管理主菜单', '#', null, 3, 'VIEW', 0, 1, 'common.admin', 'ios-hammer'),
       ('全局 - 管理 - 数据源', 'DATASOURCE', '全局：所有用户都可以访问
位置：顶部管理一级子菜单', '/admin/source', 'default', 1, 'VIEW', 3, 1, 'common.source', 'md-appstore'),
       ('全局 - 管理 - 片段', 'SNIPPET', '全局：所有用户都可以访问
位置：顶部管理一级子菜单', '/admin/snippet', null, 2, 'VIEW', 3, 1, 'common.snippet', 'ios-barcode'),
       ('全局 - 管理 - 查询历史', 'HISTORY', '全局：所有用户都可以访问
位置：顶部管理一级子菜单', '/admin/history', null, 3, 'VIEW', 3, 1, 'common.history', 'ios-book'),
       ('全局 - 管理 - 流水线', 'PIPELINE', '全局：所有用户都可以访问
位置：顶部管理一级子菜单', '/admin/pipeline', null, 4, 'VIEW', 3, 1, 'common.pipeline', 'md-list-box'),
       ('管理员 - 系统主菜单', 'SYSTEM', '管理员：管理员权限用户可以访问
位置：顶部管理一级子菜单', '#', null, 4, 'VIEW', 0, 1, 'common.system', 'md-cog'),
       ('管理员 - 系统 - 函数', 'FUNCTION', '管理员：管理员权限用户可以访问
位置：顶部管理一级子菜单', '/admin/function', null, 1, 'VIEW', 8, 1, 'common.function', 'ios-basket'),
       ('管理员 - 系统 - 定时任务', 'SCHEDULE', '管理员：管理员权限用户可以访问
位置：顶部管理一级子菜单', '/admin/schedule', null, 2, 'VIEW', 8, 1, 'common.schedule', 'md-timer'),
       ('管理员 - 系统 - 模版', 'TEMPLATE', '管理员：管理员权限用户可以访问
位置：顶部管理一级子菜单', '/admin/template', null, 3, 'VIEW', 8, 1, 'common.template', 'md-browsers'),
       ('管理员 - 系统 - 权限', 'ROLE', '管理员：管理员权限用户可以访问
位置：顶部管理一级子菜单', '/admin/role', null, 4, 'VIEW', 8, 1, 'common.authority', 'md-flag'),
       ('管理员 - 系统 - 菜单', 'MENU', '管理员：管理员权限用户可以访问
位置：顶部管理一级子菜单', '/admin/menu', null, 5, 'VIEW', 8, 1, 'common.menu', 'md-menu'),
       ('管理员 - 系统 - 用户', 'USERS', '管理员：管理员权限用户可以访问
位置：顶部管理一级子菜单', '/admin/users', null, 6, 'VIEW', 8, 1, 'common.user', 'ios-man');

insert into role_menu_relation (role_id, menu_id)
values ('2', '7'),
       ('2', '1'),
       ('2', '5'),
       ('2', '2'),
       ('2', '3'),
       ('2', '4'),
       ('2', '6'),
       ('1', '12'),
       ('1', '7'),
       ('1', '1'),
       ('1', '10'),
       ('1', '8'),
       ('1', '5'),
       ('1', '4'),
       ('1', '9'),
       ('1', '13'),
       ('1', '2'),
       ('1', '3'),
       ('1', '6'),
       ('1', '11'),
       ('1', '14');
