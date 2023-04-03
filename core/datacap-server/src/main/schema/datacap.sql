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

create fulltext index full_text_index_for_content
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
)
    comment 'Plug-in correlation function';

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

INSERT INTO datacap.role (name, description)
VALUES ('Admin', 'Admin role');
INSERT INTO datacap.role (name, description)
VALUES ('User', 'User role');

-- --------------------------------
-- Table structure for scheduled_task
-- --------------------------------
create table datacap.scheduled_task
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

INSERT INTO datacap.scheduled_task (name, description, expression, active, is_system)
VALUES ('Synchronize table structure', 'Synchronize the table structure of the data source library at 1 am every day', '0 20 * * * ?', 1, 1);

-- --------------------------------
-- Table structure for snippet
-- --------------------------------
create table datacap.snippet
(
    id          bigint auto_increment primary key,
    name        varchar(255)                        null comment ' ',
    description varchar(255)                        null comment ' ',
    code        text                                null comment ' ',
    create_time timestamp default CURRENT_TIMESTAMP not null,
    update_time timestamp default CURRENT_TIMESTAMP not null,
    user_id     bigint                              not null
);

create fulltext index full_text_index_for_code
    on datacap.snippet (code);

-- --------------------------------
-- Table structure for source
-- --------------------------------
create table datacap.source
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
    configure   text                                 null
)
    comment 'The storage is used to query the data connection source';

-- --------------------------------
-- Table structure for template_sql
-- --------------------------------
create table datacap.template_sql
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
)
    comment 'The system preset SQL template table';

INSERT INTO datacap.template_sql (name, content, description, plugin, configure, create_time, update_time, `system`)
VALUES ('getAllDatabase', 'SHOW DATABASES', 'Gets a list of all databases', 'ClickHouse,MySQL', '[]', '2022-12-08 18:38:39', '2022-12-08 18:38:39', 0);

INSERT INTO datacap.template_sql (name, content, description, plugin, configure, create_time, update_time, `system`)
VALUES ('getAllTablesFromDatabase', 'SHOW TABLES FROM ${database:String}', 'Get the data table from the database', 'ClickHouse,MySQL',
        '[{"column":"database","type":"String","expression":"${database:String}"}]', '2022-12-08 19:25:31', '2022-12-08 19:25:31', 0);

INSERT INTO datacap.template_sql (name, content, description, plugin, configure, create_time, update_time, `system`)
VALUES ('getAllDatabaseAndTable', 'SELECT database as tableSchema, name as tableName
FROM system.tables
WHERE database NOT IN (\'system\')
ORDER BY tableSchema, tableName', 'Get all databases (including all tables)', 'ClickHouse', '[]', '2022-12-08 22:52:59', '2022-12-08 22:52:59', 1);

INSERT INTO datacap.template_sql (name, content, description, plugin, configure, create_time, update_time, `system`)
VALUES ('getAllDatabaseAndTable', 'SELECT t.table_schema AS tableSchema, t.table_name AS tableName
FROM INFORMATION_SCHEMA.TABLES t
ORDER BY t.table_schema, t.table_name', 'Get all databases (including all tables)', 'MySQL', '[]', '2022-12-08 23:04:45', '2022-12-08 23:04:45', 1);

INSERT INTO datacap.template_sql (name, content, description, plugin, configure, create_time, update_time, `system`)
VALUES ('getAllDatabaseAndTables', 'show tables', 'show tables', 'ClickHouse', '[]', '2022-12-27 18:51:02', '2022-12-27 18:51:02', 0);

INSERT INTO datacap.template_sql (name, content, description, plugin, configure, create_time, update_time, `system`)
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

INSERT INTO datacap.template_sql (name, content, description, plugin, configure, create_time, update_time, `system`)
VALUES ('getAllRunningProcess', 'SELECT
  id AS id,
  info AS content,
  \'-\' AS rows,
  time AS elapsed,
  \'-\' AS bytes,
  \'-\' AS memory,
  \'-\' AS bytesRead,
  \'-\' AS bytesWritten
FROM
  information_schema.PROCESSLIST', 'Get a running list of all currently specified server nodes', 'MySQL', '[]', '2022-12-29 19:33:04', '2022-12-29 19:33:04', 0);

INSERT INTO datacap.template_sql (name, content, description, plugin, configure, create_time, update_time, `system`)
VALUES ('getAllColumnsFromDatabaseAndTable', 'DESC ${table:String}', 'Get the data column from the database and table', 'MySQL,ClickHouse',
        '[{"column":"table","type":"String","expression":"${table:String}"}]', '2023-01-10 11:59:23', '2023-01-10 11:59:23', 0);

INSERT INTO datacap.template_sql (name, content, description, plugin, configure, create_time, update_time, `system`)
VALUES ('getDataFromDatabaseAndTableLimited', 'SELECT *
FROM ${table:String}
LIMIT ${size:Integer}
OFFSET ${page:Integer}', 'Get all data from table by limited', 'MySQL,ClickHouse',
        '[{"column":"table","type":"String","expression":"${table:String}"},{"column":"size","type":"Integer","expression":"${size:Integer}"},{"column":"page","type":"Integer","expression":"${page:Integer}"}]',
        '2023-01-10 13:31:10', '2023-01-10 13:31:10', 0);

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
)
    comment 'User login log';

-- --------------------------------
-- Table structure for user_roles
-- --------------------------------
create table datacap.user_roles
(
    user_id bigint not null,
    role_id bigint not null
);

INSERT INTO datacap.user_roles (user_id, role_id)
VALUES (1, 1);

INSERT INTO datacap.user_roles (user_id, role_id)
VALUES (2, 2);

-- --------------------------------
-- Table structure for users
-- --------------------------------
create table datacap.users
(
    id              bigint auto_increment
        primary key,
    username        varchar(255)                             null comment ' ',
    password        varchar(255)                             null comment ' ',
    create_time     datetime(5) default CURRENT_TIMESTAMP(5) null,
    third_configure text                                     null
);

INSERT INTO datacap.users (username, password, create_time)
VALUES ('admin', '$2a$10$ee2yg.Te14GpHppDUROAi.HzYR5Q.q2/5vrZvAr4TFY3J2iT663JG', null);

INSERT INTO datacap.users (username, password, create_time)
VALUES ('datacap', '$2a$10$bZ4XBRlYUjKfkBovWT9TuuXlEF7lpRxVrXS8iqyCjCHUqy4RPTL8.', null);
