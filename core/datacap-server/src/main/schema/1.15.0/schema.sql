USE `datacap`;

ALTER TABLE `template_sql`
    CHANGE COLUMN `system` `is_system` BOOLEAN;

CREATE TABLE `datacap_metadata_database`
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(255),
    active      TINYINT(1) DEFAULT 1,
    create_time DATETIME,
    update_time DATETIME,
    description VARCHAR(1000),
    catalog     VARCHAR(255)
);

CREATE TABLE `datacap_metadata_database_source_relation`
(
    source_id   INT,
    database_id INT
);

CREATE TABLE `datacap_metadata_table`
(
    id             INT PRIMARY KEY AUTO_INCREMENT,
    name           VARCHAR(255),
    active         TINYINT(1) DEFAULT 1,
    create_time    DATETIME,
    update_time    DATETIME,
    type           VARCHAR(255),
    engine         VARCHAR(255),
    format         VARCHAR(255),
    in_rows        VARCHAR(255),
    in_create_time VARCHAR(255),
    in_update_time VARCHAR(255),
    collation      VARCHAR(255),
    comment        VARCHAR(1000),
    description    VARCHAR(1000)
);

CREATE TABLE `datacap_metadata_table_database_relation`
(
    table_id    INT,
    database_id INT
);

CREATE TABLE `datacap_metadata_column`
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    name           VARCHAR(255),
    active         TINYINT(1) DEFAULT 1,
    create_time    DATETIME,
    update_time    DATETIME,
    description    VARCHAR(1000),
    type           TEXT,
    comment        VARCHAR(1000),
    default_value  VARCHAR(500),
    position       VARCHAR(100),
    is_nullable    VARCHAR(100),
    maximum_length VARCHAR(100),
    collation      VARCHAR(100),
    is_key         VARCHAR(100),
    privileges     VARCHAR(1000),
    data_type      VARCHAR(100),
    extra          VARCHAR(250)
);

CREATE TABLE `datacap_metadata_column_table_relation`
(
    table_id  INT,
    column_id INT
);

INSERT INTO `template_sql` (`name`, `content`, `description`, `plugin`, `configure`, `create_time`, `update_time`, `is_system`)
VALUES ('SYSTEM_FOR_GET_ALL_DATABASES',
        'SELECT\n    `CATALOG_NAME` AS \"CATALOG_NODE\",\n    `SCHEMA_NAME` AS \"SCHEMA_NODE\",\n    `DEFAULT_CHARACTER_SET_NAME` AS \"CHARSET_NODE\"\nFROM\n    INFORMATION_SCHEMA.SCHEMATA',
        'Find a list of databases that already exist in all services\n查找所有服务中已经存在的数据库列表', 'MySQL', '[]', '2023-09-15 16:47:13', '2023-09-15 17:02:37', 1);

INSERT INTO `template_sql` (`name`, `content`, `description`, `plugin`, `configure`, `create_time`, `update_time`, `is_system`)
VALUES ('SYSTEM_FOR_GET_ALL_TABLES', 'SELECT
    `TABLE_CATALOG` AS "CATALOG_NODE",
    `TABLE_SCHEMA` AS "SCHEMA_NODE",
    `TABLE_NAME` AS "TABLE_NODE",
    CASE `TABLE_TYPE`
        WHEN \'BASE TABLE\' THEN \'TABLE\'
        WHEN \'VIEW\' THEN \'VIEW\'
        WHEN \'SYSTEM VIEW\' THEN \'SYSTEM\'
    END AS "TYPE_NODE",
    `ENGINE` AS "ENGINE_NODE",
    `ROW_FORMAT` AS "FORMAT_NODE",
    `TABLE_ROWS` AS "ROWS_NODE",
    `CREATE_TIME` AS "CREATE_TIME_NODE",
    `UPDATE_TIME` AS "UPDATE_TIME_NODE",
    `TABLE_COLLATION` AS "COLLATION_NODE",
    `TABLE_COMMENT` AS "COMMENT_NODE",
    `AVG_ROW_LENGTH` AS "AVG_ROW_NODE",
    `DATA_LENGTH` AS "DATA_NODE",
    `INDEX_LENGTH` AS "INDEX_NODE",
    `AUTO_INCREMENT` AS "AUTO_INCREMENT_NODE"
FROM
    information_schema.TABLES
WHERE
    TABLE_SCHEMA = \'${database:String}\'', 'Queries the list of tables in the library according to the specified data name
根据指定数据名查询该库下数据表列表', 'MySQL',
        '[{"column":"database","type":"String","expression":"${database:String}"}]', '2023-09-18 20:10:53',
        '2023-10-09 16:35:18', 1);

INSERT INTO `template_sql` (`name`, `content`, `description`, `plugin`, `configure`, `create_time`, `update_time`, `is_system`)
VALUES ('SYSTEM_FOR_GET_ALL_COLUMNS', 'SELECT
    `TABLE_CATALOG` AS "CATALOG_NODE",
    `TABLE_SCHEMA` AS "SCHEMA_NODE",
    `COLUMN_NAME` AS "COLUMN_NODE",
    `COLUMN_DEFAULT` AS "DEFAULT_NODE",
    `ORDINAL_POSITION` AS "POSITION_NODE",
    CASE `IS_NULLABLE`
        WHEN \'YES\' THEN TRUE
        WHEN \'NO\' THEN FALSE
    END AS "NULLABLE_NODE",
    `DATA_TYPE` AS "DATA_TYPE_NODE",
    `COLUMN_TYPE` AS "COLUMN_TYPE_NODE",
    `EXTRA` AS "EXTRA_NODE",
    `CHARACTER_MAXIMUM_LENGTH` AS "MAXIMUM_LENGTH_NODE",
    `CHARACTER_OCTET_LENGTH` AS "OCTET_LENGTH_NODE",
    `NUMERIC_PRECISION` AS "PRECISION_NODE",
    `NUMERIC_SCALE` AS "SCALE_NODE",
    `NUMERIC_SCALE` AS "SCALE_NODE",
    `CHARACTER_SET_NAME` AS "CHARACTER_NODE",
    `COLLATION_NAME` AS "COLLATION_NODE",
    `COLUMN_KEY` AS "KEY_NODE",
    `PRIVILEGES` AS "PRIVILEGES_NODE",
    `COLUMN_COMMENT` AS "COMMENT_NODE"
FROM
    information_schema.COLUMNS
WHERE
    TABLE_SCHEMA = \'${database:String}\'
AND TABLE_NAME = \'${table:String}\';
', 'According to the specified database, the data table gets all the data columns under the current condition
根据指定数据库，数据表获取当前条件下所有数据列', 'MySQL',
        '[{"column":"database","type":"String","expression":"${database:String}"},{"column":"table","type":"String","expression":"${table:String}"}]',
        '2023-09-18 23:40:26', '2023-10-09 16:40:41', 1);

CREATE TABLE `datacap_scheduled_history`
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(255),
    active      BOOLEAN DEFAULT TRUE,
    create_time DATETIME,
    update_time DATETIME,
    info        TEXT,
    state       VARCHAR(100)
);

CREATE TABLE `datacap_scheduled_history_relation`
(
    scheduled_id INT,
    history_id   INT
);


