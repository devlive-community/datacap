INSERT INTO scheduled_task (name, description, expression, active, is_system)
VALUES ('Synchronize table structure', 'Synchronize the table structure of the data source library at 1 am every day', '0 20 * * * ?', TRUE, TRUE);

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

CREATE TABLE `menus`
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
    `create_time` date         DEFAULT CURRENT_TIMESTAMP(5),
    `update_time` date         DEFAULT CURRENT_TIMESTAMP(5)
);
