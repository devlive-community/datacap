-- --------------------------------
-- Table for audit_plugin
-- --------------------------------
CREATE TABLE IF NOT EXISTS audit_plugin
(
    id          bigint PRIMARY KEY AUTO_INCREMENT,
    state       varchar(255) NULL,
    create_time mediumtext   NULL,
    end_time    mediumtext   NULL,
    plugin_id   bigint       NOT NULL,
    content     text         NULL,
    message     text         NULL,
    elapsed     bigint       NULL DEFAULT 0,
    user_id     bigint       NOT NULL
);
-- --------------------------------
-- Table for functions
-- --------------------------------
CREATE TABLE IF NOT EXISTS functions
(
    id          bigint PRIMARY KEY AUTO_INCREMENT,
    name        varchar(255) NULL COMMENT 'Function name',
    content     varchar(255) NULL COMMENT 'Expression of function',
    description text         NULL COMMENT 'Function description',
    plugin      varchar(255) NULL COMMENT 'Trial plug-in, multiple according to, split',
    example     text         NULL COMMENT 'Function Usage Example',
    create_time datetime     NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    update_time datetime     NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    type        varchar(20)  NULL DEFAULT 'KEYWORDS'
);
-- --------------------------------
-- Table for pipeline
-- --------------------------------
CREATE TABLE IF NOT EXISTS pipeline
(
    id              int PRIMARY KEY AUTO_INCREMENT,
    name            varchar(255) NOT NULL,
    content         text         NOT NULL,
    state           varchar(100) NULL,
    message         text         NULL,
    work            text         NULL,
    start_time      datetime     NULL DEFAULT CURRENT_TIMESTAMP,
    end_time        datetime     NULL ON UPDATE CURRENT_TIMESTAMP,
    elapsed         bigint       NULL,
    user_id         int          NOT NULL,
    from_id         int          NOT NULL,
    from_configures text         NULL,
    to_id           int          NOT NULL,
    to_configures   text         NULL
);
-- --------------------------------
-- Table structure for role
-- --------------------------------
CREATE TABLE IF NOT EXISTS role
(
    id          bigint PRIMARY KEY AUTO_INCREMENT,
    name        varchar(255) NULL COMMENT ' ',
    description varchar(255) NULL COMMENT ' ',
    create_time datetime(5)  NULL DEFAULT CURRENT_TIMESTAMP(5)
);
TRUNCATE TABLE role;
ALTER TABLE role
    ALTER COLUMN id RESTART WITH 1;
INSERT INTO role (name, description)
VALUES ('Admin', 'Admin role');
INSERT INTO role (name, description)
VALUES ('User', 'User role');
-- --------------------------------
-- Table structure for scheduled_task
-- --------------------------------
CREATE TABLE IF NOT EXISTS scheduled_task
(
    id          int PRIMARY KEY AUTO_INCREMENT,
    name        varchar(255) NOT NULL,
    description text         NOT NULL,
    expression  varchar(100) NULL,
    active      boolean      NULL DEFAULT 1,
    is_system   boolean      NULL DEFAULT 1,
    create_time date         NULL DEFAULT CURRENT_TIMESTAMP,
    update_time date         NULL ON UPDATE CURRENT_TIMESTAMP
);
TRUNCATE TABLE scheduled_task;
ALTER TABLE scheduled_task
    ALTER COLUMN id RESTART WITH 1;
-- --------------------------------
-- Table structure for snippet
-- --------------------------------
CREATE TABLE IF NOT EXISTS snippet
(
    id          bigint PRIMARY KEY AUTO_INCREMENT,
    name        varchar(255) NULL COMMENT ' ',
    description varchar(255) NULL COMMENT ' ',
    code        text         NULL COMMENT ' ',
    create_time timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id     bigint       NOT NULL
);
-- --------------------------------
-- Table structure for source
-- --------------------------------
CREATE TABLE IF NOT EXISTS source
(
    id          bigint PRIMARY KEY AUTO_INCREMENT,
    _catalog    varchar(255) NULL,
    create_time date         NULL DEFAULT CURRENT_TIMESTAMP,
    _database   varchar(255) NULL,
    description varchar(255) NULL,
    host        varchar(255) NOT NULL,
    name        varchar(255) NOT NULL,
    password    varchar(255) NULL,
    port        bigint       NOT NULL,
    protocol    varchar(255) NULL,
    username    varchar(255) NULL,
    _type       varchar(100) NOT NULL,
    `ssl`       boolean      NULL DEFAULT 0,
    _ssl        boolean      NULL DEFAULT 0,
    publish     boolean      NULL DEFAULT 0,
    public      boolean      NULL DEFAULT 0,
    user_id     bigint       NULL,
    configure   text         NULL,
    used_config boolean           default false
);
TRUNCATE TABLE scheduled_task;
ALTER TABLE scheduled_task
    ALTER COLUMN id RESTART WITH 1;
INSERT INTO source(name, _database, password, host, port, protocol, username, _type, publish, user_id, public)
VALUES ('Built-in database', 'datacap', 'h2', '-', 1, 'NATIVE', 'h2', 'H2', FALSE, 2, TRUE);
-- --------------------------------
-- Table structure for template_sql
-- --------------------------------
CREATE TABLE IF NOT EXISTS template_sql
(
    id          bigint PRIMARY KEY AUTO_INCREMENT,
    name        varchar(255) NULL COMMENT 'Name of template',
    content     text         NULL,
    description text         NULL,
    plugin      varchar(50)  NULL COMMENT 'Using plug-ins',
    configure   text         NULL COMMENT 'The template must use the configuration information in the key->value format',
    create_time timestamp    DEFAULT CURRENT_TIMESTAMP,
    update_time timestamp    DEFAULT CURRENT_TIMESTAMP,
    `system`    boolean      NULL     DEFAULT 0
);
TRUNCATE TABLE template_sql;
ALTER TABLE template_sql
    ALTER COLUMN id RESTART WITH 1;
INSERT INTO template_sql ( name, content, description, plugin, configure
                         , create_time, update_time, `system`)
VALUES ( 'getAllDatabase', 'SHOW DATABASES', 'Gets a list of all databases', 'ClickHouse,MySQL,H2', '[]'
       , '2022-12-08 18:38:39', '2022-12-08 18:38:39', 1);
INSERT INTO template_sql ( name, content, description, plugin, configure
                         , create_time, update_time, `system`)
VALUES ( 'getAllTablesFromDatabase', 'SHOW TABLES FROM ${database:String}', 'Get the data table from the database', 'ClickHouse,MySQL,H2'
       , '[{"column":"database","type":"String","expression":"${database:String}"}]'
       , '2022-12-08 19:25:31', '2022-12-08 19:25:31', 1);
INSERT INTO template_sql ( name, content, description, plugin, configure
                         , create_time, update_time, `system`)
VALUES ( 'getAllDatabaseAndTable', 'SELECT database as tableSchema, name as tableName
FROM system.tables
WHERE database NOT IN (''system'')
ORDER BY tableSchema, tableName', 'Get all databases (including all tables)', 'ClickHouse', '[]'
       , '2022-12-08 22:52:59', '2022-12-08 22:52:59', 1);
INSERT INTO template_sql ( name, content, description, plugin, configure
                         , create_time, update_time, `system`)
VALUES ( 'getAllDatabaseAndTable', 'SELECT t.table_schema AS tableSchema, t.table_name AS tableName
FROM INFORMATION_SCHEMA.TABLES t
ORDER BY t.table_schema, t.table_name', 'Get all databases (including all tables)', 'MySQL', '[]'
       , '2022-12-08 23:04:45', '2022-12-08 23:04:45', 1);
INSERT INTO template_sql ( name, content, description, plugin, configure
                         , create_time, update_time, `system`)
VALUES ( 'getAllDatabaseAndTables', 'show tables', 'show tables', 'ClickHouse', '[]'
       , '2022-12-27 18:51:02', '2022-12-27 18:51:02', 0);
INSERT INTO template_sql ( name, content, description, plugin, configure
                         , create_time, update_time, `system`)
VALUES ( 'getAllRunningProcess', 'SELECT
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
  round(elapsed, 1) > 0', 'Get a running list of all currently specified server nodes', 'ClickHouse', '[]'
       , '2022-12-29 16:40:11', '2022-12-29 16:40:11', 0);
INSERT INTO template_sql ( name, content, description, plugin, configure
                         , create_time, update_time, `system`)
VALUES ( 'getAllRunningProcess', 'SELECT
  id AS id,
  info AS content,
  ''-'' AS rows,
  time AS elapsed,
  ''-'' AS bytes,
  ''-'' AS memory,
  ''-'' AS bytesRead,
  ''-'' AS bytesWritten
FROM
  information_schema.PROCESSLIST', 'Get a running list of all currently specified server nodes', 'MySQL', '[]'
       , '2022-12-29 19:33:04', '2022-12-29 19:33:04', 0);
INSERT INTO template_sql ( name, content, description, plugin, configure
                         , create_time, update_time, `system`)
VALUES ( 'getAllColumnsFromDatabaseAndTable', 'DESC ${table:String}', 'Get the data column from the database and table', 'MySQL,ClickHouse'
       , '[{"column":"table","type":"String","expression":"${table:String}"}]'
       , '2023-01-10 11:59:23', '2023-01-10 11:59:23', 0);
INSERT INTO template_sql (name, content, description, plugin, configure, `system`)
VALUES ( 'getAllColumnsFromDatabaseAndTable', 'SHOW COLUMNS FROM ${table:String}', 'Get the data column from the database and table', 'H2'
       , '[{"column":"table","type":"String","expression":"${table:String}"}]', 1);
INSERT INTO template_sql ( name, content, description, plugin, configure
                         , create_time, update_time, `system`)
VALUES ( 'getDataFromDatabaseAndTableLimited', 'SELECT *
FROM ${table:String}
LIMIT ${size:Integer}
OFFSET ${page:Integer}', 'Get all data from table by limited', 'MySQL,ClickHouse'
       , '[{"column":"table","type":"String","expression":"${table:String}"},{"column":"size","type":"Integer","expression":"${size:Integer}"},{"column":"page","type":"Integer","expression":"${page:Integer}"}]'
       , '2023-01-10 13:31:10', '2023-01-10 13:31:10', 0);
-- --------------------------------
-- Table structure for user_chat
-- --------------------------------
CREATE TABLE IF NOT EXISTS user_chat
(
    id          int PRIMARY KEY AUTO_INCREMENT,
    name        varchar(255) NOT NULL,
    question    text         NOT NULL,
    answer      text         NULL,
    type        varchar(100) NULL,
    create_time date         NULL DEFAULT CURRENT_TIMESTAMP,
    end_time    date         NULL ON UPDATE CURRENT_TIMESTAMP,
    elapsed     bigint       NULL,
    user_id     int          NOT NULL,
    is_new      boolean      NULL DEFAULT 1
);
-- --------------------------------
-- Table structure for user_log
-- --------------------------------
CREATE TABLE IF NOT EXISTS user_log
(
    id          bigint PRIMARY KEY AUTO_INCREMENT,
    device      varchar(255) NULL COMMENT 'Login device',
    client      varchar(255) NULL COMMENT 'Login client',
    ip          varchar(100) NULL COMMENT 'Login ip',
    message     varchar(225) NULL COMMENT 'Error message',
    state       varchar(20)  NULL COMMENT 'Login state',
    ua          varchar(255) NULL COMMENT 'Trial plug-in, multiple according to, split',
    user_id     bigint       NOT NULL,
    create_time date         NULL DEFAULT CURRENT_TIMESTAMP(5)
);
-- --------------------------------
-- Table structure for user_roles
-- --------------------------------
CREATE TABLE IF NOT EXISTS user_roles
(
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);
TRUNCATE TABLE user_roles;
INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id)
VALUES (2, 2);
-- --------------------------------
-- Table structure for users
-- --------------------------------
CREATE TABLE IF NOT EXISTS users
(
    id              bigint PRIMARY KEY AUTO_INCREMENT,
    username        varchar(255) NULL COMMENT ' ',
    password        varchar(255) NULL COMMENT ' ',
    create_time     date         NULL DEFAULT CURRENT_TIMESTAMP(5),
    third_configure text         NULL
);
TRUNCATE TABLE users;
ALTER TABLE users
    ALTER COLUMN id RESTART WITH 1;
INSERT INTO users (username, password)
VALUES ('admin', '$2a$10$ee2yg.Te14GpHppDUROAi.HzYR5Q.q2/5vrZvAr4TFY3J2iT663JG');
INSERT INTO users (username, password)
VALUES ('datacap', '$2a$10$bZ4XBRlYUjKfkBovWT9TuuXlEF7lpRxVrXS8iqyCjCHUqy4RPTL8.');

-- --------------------------------
--       Update to 1.11.0        --
-- --------------------------------
alter table `audit_plugin`
    add column `count` bigint default 0;
