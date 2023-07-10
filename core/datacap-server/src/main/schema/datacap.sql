use datacap;

-- --------------------------------
-- Table for audit_plugin
-- --------------------------------
create table audit_plugin
(
    id          bigint auto_increment primary key,
    state       varchar(255)     null,
    create_time mediumtext       null,
    end_time    mediumtext       null,
    plugin_id   bigint           not null,
    content     text             null,
    message     text             null,
    elapsed     bigint default 0 null,
    user_id     bigint           not null
);

create
    fulltext index full_text_index_for_content
    on audit_plugin (content);

-- --------------------------------
-- Table for functions
-- --------------------------------
create table functions
(
    id          bigint auto_increment primary key,
    name        varchar(255)                          null comment 'Function name',
    content     varchar(255)                          null comment 'Expression of function',
    description text                                  null comment 'Function description',
    plugin      varchar(255)                          null comment 'Trial plug-in, multiple according to, split',
    example     text                                  null comment 'Function Usage Example',
    create_time datetime    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    update_time datetime    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    type        varchar(20) default 'KEYWORDS'        null
) comment 'Plug-in correlation function';

-- --------------------------------
-- Table for pipeline
-- --------------------------------
create table pipeline
(
    id              int auto_increment primary key,
    name            varchar(255)                       not null,
    content         text                               not null,
    state           varchar(100)                       null,
    message         text                               null,
    work            text                               null,
    start_time      datetime default CURRENT_TIMESTAMP null,
    end_time        datetime                           null on update CURRENT_TIMESTAMP,
    elapsed         bigint                             null,
    user_id         int                                not null,
    from_id         int                                not null,
    from_configures text                               null,
    to_id           int                                not null,
    to_configures   text                               null
);

-- --------------------------------
-- Table structure for role
-- --------------------------------
create table role
(
    id          bigint auto_increment primary key,
    name        varchar(255)                             null comment ' ',
    description varchar(255)                             null comment ' ',
    create_time datetime(5) default CURRENT_TIMESTAMP(5) null
);

INSERT INTO role (name, description)
VALUES ('Admin', 'Admin role');
INSERT INTO role (name, description)
VALUES ('User', 'User role');

-- --------------------------------
-- Table structure for scheduled_task
-- --------------------------------
create table scheduled_task
(
    id          int auto_increment primary key,
    name        varchar(255)                         not null,
    description text                                 not null,
    expression  varchar(100)                         null,
    active      tinyint(1) default 1                 null,
    is_system   tinyint(1) default 1                 null,
    create_time datetime   default CURRENT_TIMESTAMP null,
    update_time datetime                             null on update CURRENT_TIMESTAMP
);

INSERT INTO scheduled_task (name, description, expression, active, is_system)
VALUES ('Synchronize table structure', 'Synchronize the table structure of the data source library at 1 am every day', '0 20 * * * ?', 1, 1);

-- --------------------------------
-- Table structure for snippet
-- --------------------------------
create table snippet
(
    id          bigint auto_increment primary key,
    name        varchar(255)                        null comment ' ',
    description varchar(255)                        null comment ' ',
    code        text                                null comment ' ',
    create_time timestamp default CURRENT_TIMESTAMP not null,
    update_time timestamp default CURRENT_TIMESTAMP not null,
    user_id     bigint                              not null
);

create
    fulltext index full_text_index_for_code
    on snippet (code);

-- --------------------------------
-- Table structure for source
-- --------------------------------
create table source
(
    id          bigint auto_increment primary key,
    _catalog    varchar(255)                         null,
    create_time datetime   default CURRENT_TIMESTAMP null,
    _database   varchar(255)                         null,
    description varchar(255)                         null,
    host        varchar(255)                         not null,
    name        varchar(255)                         not null,
    password    varchar(255)                         null,
    port        bigint                               not null,
    protocol    varchar(255)                         null,
    username    varchar(255)                         null,
    _type       varchar(100)                         not null,
    `ssl`       tinyint(1) default 0                 null,
    _ssl        tinyint(1) default 0                 null,
    publish     tinyint(1) default 0                 null,
    public      tinyint(1) default 0                 null,
    user_id     bigint                               null,
    configure   text                                 null,
    used_config boolean    default false
) comment 'The storage is used to query the data connection source';

-- --------------------------------
-- Table structure for template_sql
-- --------------------------------
create table template_sql
(
    id          bigint auto_increment primary key,
    name        varchar(255)                         null comment 'Name of template',
    content     text                                 null,
    description text                                 null,
    plugin      varchar(50)                          null comment 'Using plug-ins',
    configure   text                                 null comment 'The template must use the configuration information in the key->value format',
    create_time timestamp  default CURRENT_TIMESTAMP not null,
    update_time timestamp  default CURRENT_TIMESTAMP not null,
    `system`    tinyint(1) default 0                 null
) comment 'The system preset SQL template table';

INSERT INTO template_sql (name, content, description, plugin, configure, create_time, update_time, `system`)
VALUES ('getAllDatabase', 'SHOW DATABASES', 'Gets a list of all databases', 'ClickHouse,MySQL,H2', '[]', '2022-12-08 18:38:39', '2022-12-08 18:38:39', 1);

INSERT INTO template_sql (name, content, description, plugin, configure, create_time, update_time, `system`)
VALUES ('getAllTablesFromDatabase', 'SHOW TABLES FROM ${database:String}', 'Get the data table from the database', 'ClickHouse,MySQL,H2',
        '[{"column":"database","type":"String","expression":"${database:String}"}]', '2022-12-08 19:25:31', '2022-12-08 19:25:31', 1);

INSERT INTO template_sql (name, content, description, plugin, configure, create_time, update_time, `system`)
VALUES ('getAllDatabaseAndTable', 'SELECT database as tableSchema, name as tableName
FROM system.tables
WHERE database NOT IN (\'system\')
ORDER BY tableSchema, tableName', 'Get all databases (including all tables)', 'ClickHouse', '[]', '2022-12-08 22:52:59', '2022-12-08 22:52:59', 1);

INSERT INTO template_sql (name, content, description, plugin, configure, create_time, update_time, `system`)
VALUES ('getAllDatabaseAndTable', 'SELECT t.table_schema AS tableSchema, t.table_name AS tableName
FROM INFORMATION_SCHEMA.TABLES t
ORDER BY t.table_schema, t.table_name', 'Get all databases (including all tables)', 'MySQL', '[]', '2022-12-08 23:04:45', '2022-12-08 23:04:45', 1);

INSERT INTO template_sql (name, content, description, plugin, configure, create_time, update_time, `system`)
VALUES ('getAllDatabaseAndTables', 'show tables', 'show tables', 'ClickHouse', '[]', '2022-12-27 18:51:02', '2022-12-27 18:51:02', 0);

INSERT INTO template_sql (name, content, description, plugin, configure, create_time, update_time, `system`)
VALUES ('getAllRunningProcess', 'SELECT
  query_id AS id,
  query AS content,
  toUInt64(toUInt64(read_rows) + toUInt64(written_rows)) AS rows,
  round(elapsed, 1) AS elapsed,
  formatReadableSize(toUInt64(read_bytes) + toUInt64(written_bytes)) AS bytes,
  formatReadableSize(memory_usage) AS memory,
  formatReadableSize(read_bytes) AS bytesRead,
  formatReadableSize(written_bytes) AS bytesWritten
FROM
  system.processes
WHERE
  round(elapsed, 1) > 0', 'Get a running list of all currently specified server nodes', 'ClickHouse', '[]', '2022-12-29 16:40:11', '2022-12-29 16:40:11', 0);

INSERT INTO template_sql (name, content, description, plugin, configure, create_time, update_time, `system`)
VALUES ('getAllRunningProcess', 'SELECT
  id AS id,
  info AS content,
  \' -\' AS rows,
  time AS elapsed,
  \' -\' AS bytes,
  \' -\' AS memory,
  \' -\' AS bytesRead,
  \' -\' AS bytesWritten
FROM
  information_schema.PROCESSLIST', 'Get a running list of all currently specified server nodes', 'MySQL', '[]', '2022-12-29 19:33:04', '2022-12-29 19:33:04', 0);

INSERT INTO template_sql (name, content, description, plugin, configure, create_time, update_time, `system`)
VALUES ('getAllColumnsFromDatabaseAndTable', 'DESC ${table:String}', 'Get the data column from the database and table', 'MySQL,ClickHouse',
        '[{"column":"table","type":"String","expression":"${table:String}"}]', '2023-01-10 11:59:23', '2023-01-10 11:59:23', 0);

INSERT INTO template_sql (name, content, description, plugin, configure, create_time, update_time, `system`)
VALUES ('getDataFromDatabaseAndTableLimited', 'SELECT *
FROM ${table:String}
LIMIT ${size:Integer}
OFFSET ${page:Integer}', 'Get all data from table by limited', 'MySQL,ClickHouse',
        '[{"column":"table","type":"String","expression":"${table:String}"},{"column":"size","type":"Integer","expression":"${size:Integer}"},{"column":"page","type":"Integer","expression":"${page:Integer}"}]',
        '2023-01-10 13:31:10', '2023-01-10 13:31:10', 0);

INSERT INTO template_sql (name, content, description, plugin, configure, `system`)
VALUES ( 'getAllColumnsFromDatabaseAndTable', 'SHOW COLUMNS FROM ${table:String}', 'Get the data column from the database and table', 'H2'
       , '[{"column":"table","type":"String","expression":"${table:String}"}]', 1);

-- --------------------------------
-- Table structure for user_chat
-- --------------------------------
create table user_chat
(
    id          int auto_increment primary key,
    name        varchar(255)                         not null,
    question    text                                 not null,
    answer      text                                 null,
    type        varchar(100)                         null,
    create_time datetime   default CURRENT_TIMESTAMP null,
    end_time    datetime                             null on update CURRENT_TIMESTAMP,
    elapsed     bigint                               null,
    user_id     int                                  not null,
    is_new      tinyint(1) default 1                 null
);

-- --------------------------------
-- Table structure for user_log
-- --------------------------------
create table user_log
(
    id          bigint auto_increment
        primary key,
    device      varchar(255)                             null comment 'Login device',
    client      varchar(255)                             null comment 'Login client',
    ip          varchar(100)                             null comment 'Login ip',
    message     varchar(225)                             null comment 'Error message',
    state       varchar(20)                              null comment 'Login state',
    ua          varchar(255)                             null comment 'Trial plug-in, multiple according to, split',
    user_id     bigint                                   not null,
    create_time datetime(5) default CURRENT_TIMESTAMP(5) null
) comment 'User login log';

-- --------------------------------
-- Table structure for user_roles
-- --------------------------------
create table user_roles
(
    user_id bigint not null,
    role_id bigint not null
);

INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1);

INSERT INTO user_roles (user_id, role_id)
VALUES (2, 2);

-- --------------------------------
-- Table structure for users
-- --------------------------------
create table users
(
    id              bigint auto_increment
        primary key,
    username        varchar(255)                             null comment ' ',
    password        varchar(255)                             null comment ' ',
    create_time     datetime(5) default CURRENT_TIMESTAMP(5) null,
    third_configure text                                     null
);

INSERT INTO users (username, password)
VALUES ('admin', '$2a$10$ee2yg.Te14GpHppDUROAi.HzYR5Q.q2/5vrZvAr4TFY3J2iT663JG');

INSERT INTO users (username, password)
VALUES ('datacap', '$2a$10$bZ4XBRlYUjKfkBovWT9TuuXlEF7lpRxVrXS8iqyCjCHUqy4RPTL8.');

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
    `i18n_key`    varchar(255) DEFAULT NULL,
    `icon`        varchar(255) DEFAULT NULL,
    `create_time` datetime     DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime     DEFAULT CURRENT_TIMESTAMP
);

insert into `menus` (name, code, description, url, group_name, sorted, type, parent, active, i18n_key, icon)
values ('全局 - 首页', 'HOME', '全局路由：所有用户都可以访问', '/dashboard/index', null, 1, 'VIEW', 0, 1, 'common.home', 'ios-navigate'),
       ('全局 - 查询', 'QUERY', '全局路由：所有用户都可以访问', '/console/index', null, 2, 'VIEW', 0, 1, 'common.query', 'md-browsers'),
       ('全局 - 管理主菜单', 'MANAGEMENT', '全局：所有用户都可以访问
位置：顶部管理主菜单', '/admin', null, 3, 'VIEW', 0, 1, 'common.admin', 'ios-hammer'),
       ('全局 - 管理 - 数据源', 'DATASOURCE', '全局：所有用户都可以访问
位置：顶部管理一级子菜单', '/admin/source', 'default', 1, 'VIEW', 3, 1, 'common.source', 'md-appstore'),
       ('全局 - 管理 - 片段', 'SNIPPET', '全局：所有用户都可以访问
位置：顶部管理一级子菜单', '/admin/snippet', null, 2, 'VIEW', 3, 1, 'common.snippet', 'ios-barcode'),
       ('全局 - 管理 - 查询历史', 'HISTORY', '全局：所有用户都可以访问
位置：顶部管理一级子菜单', '/admin/history', null, 3, 'VIEW', 3, 1, 'common.history', 'ios-book'),
       ('全局 - 管理 - 流水线', 'PIPELINE', '全局：所有用户都可以访问
位置：顶部管理一级子菜单', '/admin/pipeline', null, 4, 'VIEW', 3, 1, 'common.pipeline', 'md-list-box'),
       ('管理员 - 系统主菜单', 'SYSTEM', '管理员：管理员权限用户可以访问
位置：顶部管理一级子菜单', '/system', null, 4, 'VIEW', 0, 1, 'common.system', 'md-cog'),
       ('管理员 - 系统 - 函数', 'FUNCTION', '管理员：管理员权限用户可以访问
位置：顶部管理一级子菜单', '/system/function', null, 1, 'VIEW', 8, 1, 'common.function', 'ios-basket'),
       ('管理员 - 系统 - 定时任务', 'SCHEDULE', '管理员：管理员权限用户可以访问
位置：顶部管理一级子菜单', '/system/schedule', null, 2, 'VIEW', 8, 1, 'common.schedule', 'md-timer'),
       ('管理员 - 系统 - 模版', 'TEMPLATE', '管理员：管理员权限用户可以访问
位置：顶部管理一级子菜单', '/system/template', null, 3, 'VIEW', 8, 1, 'common.template', 'md-browsers'),
       ('管理员 - 系统 - 权限', 'ROLE', '管理员：管理员权限用户可以访问
位置：顶部管理一级子菜单', '/system/role', null, 4, 'VIEW', 8, 1, 'common.authority', 'md-flag'),
       ('管理员 - 系统 - 菜单', 'MENU', '管理员：管理员权限用户可以访问
位置：顶部管理一级子菜单', '/system/menu', null, 5, 'VIEW', 8, 1, 'common.menu', 'md-menu'),
       ('管理员 - 系统 - 用户', 'USERS', '管理员：管理员权限用户可以访问
位置：顶部管理一级子菜单', '/system/user', null, 6, 'VIEW', 8, 1, 'common.user', 'ios-man');

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

-- --------------------------------
--       Update to 1.11.0        --
-- --------------------------------
alter table `audit_plugin`
    add column `count` bigint(20) default 0;

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

-- --------------------------------
--       Update to 1.12.0        --
-- --------------------------------
ALTER TABLE `menus`
    ADD COLUMN `redirect` BIGINT  DEFAULT 0,
    ADD COLUMN `is_new`   BOOLEAN DEFAULT FALSE;

RENAME TABLE `users` TO `datacap_user`;

ALTER TABLE `datacap_user`
    ADD COLUMN `is_system` BOOLEAN DEFAULT FALSE;

INSERT INTO `datacap_user`(`username`, `is_system`)
VALUES ('Ai', TRUE);

CREATE TABLE `datacap_chat`
(
    `id`          BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name`        VARCHAR(255) NOT NULL,
    `active`      BOOLEAN      DEFAULT TRUE,
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP,
    `avatar`      VARCHAR(255) DEFAULT NULL,
    `description` VARCHAR(255) DEFAULT NULL
);

CREATE TABLE `datacap_chat_user_relation`
(
    chat_id BIGINT,
    user_id BIGINT
);

CREATE TABLE `datacap_message`
(
    `id`                BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name`              VARCHAR(255) NOT NULL,
    `active`            BOOLEAN      DEFAULT TRUE,
    `create_time`       DATETIME     DEFAULT CURRENT_TIMESTAMP,
    `update_time`       DATETIME     DEFAULT CURRENT_TIMESTAMP,
    `content`           TEXT         DEFAULT NULL,
    `model`             VARCHAR(255) DEFAULT NULL,
    `type`              VARCHAR(100),
    `prompt_tokens`     BIGINT       DEFAULT 0,
    `completion_tokens` BIGINT       DEFAULT 0,
    `total_tokens`      BIGINT       DEFAULT 0
);

CREATE TABLE `datacap_message_user_relation`
(
    message_id BIGINT,
    user_id    BIGINT
);

CREATE TABLE `datacap_message_chat_relation`
(
    message_id BIGINT,
    chat_id    BIGINT
);
