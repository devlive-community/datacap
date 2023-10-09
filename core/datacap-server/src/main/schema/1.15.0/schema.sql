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
VALUES ('SYSTEM_FOR_GET_ALL_TABLES',
        'SELECT\n    `TABLE_CATALOG` AS \"CATALOG_NODE\",\n    `TABLE_SCHEMA` AS \"SCHEMA_NODE\",\n    `TABLE_NAME` AS \"TABLE_NODE\",\n    `TABLE_TYPE` AS \"TYPE_NODE\",\n    `ENGINE` AS \"ENGINE_NODE\",\n    `ROW_FORMAT` AS \"FORMAT_NODE\",\n    `TABLE_ROWS` AS \"ROWS_NODE\",\n    `CREATE_TIME` AS \"CREATE_TIME_NODE\",\n    `UPDATE_TIME` AS \"UPDATE_TIME_NODE\",\n    `TABLE_COLLATION` AS \"COLLATION_NODE\",\n    `TABLE_COMMENT` AS \"COMMENT_NODE\"\nFROM\n    information_schema.TABLES\nWHERE\n    TABLE_SCHEMA = \'${database:String}\'',
        'Queries the list of tables in the library according to the specified data name\n根据指定数据名查询该库下数据表列表', 'MySQL',
        '[{\"column\":\"database\",\"type\":\"String\",\"expression\":\"${database:String}\"}]', '2023-09-18 20:10:53', '2023-09-18 20:11:10', 1);
INSERT INTO `template_sql` (`name`, `content`, `description`, `plugin`, `configure`, `create_time`, `update_time`, `is_system`)
VALUES ('SYSTEM_FOR_GET_ALL_COLUMNS',
        'SELECT\n    `TABLE_CATALOG` AS \"CATALOG_NODE\",\n    `TABLE_SCHEMA` AS \"SCHEMA_NODE\",\n    `COLUMN_NAME` AS \"COLUMN_NODE\",\n    `COLUMN_DEFAULT` AS \"DEFAULT_NODE\",\n    `ORDINAL_POSITION` AS \"POSITION_NODE\",\n    `IS_NULLABLE` AS \"NULLABLE_NODE\",\n    `DATA_TYPE` AS \"TYPE_NODE\",\n    `CHARACTER_MAXIMUM_LENGTH` AS \"MAXIMUM_LENGTH_NODE\",\n    `CHARACTER_OCTET_LENGTH` AS \"OCTET_LENGTH_NODE\",\n    `NUMERIC_PRECISION` AS \"PRECISION_NODE\",\n    `NUMERIC_SCALE` AS \"SCALE_NODE\",\n    `NUMERIC_SCALE` AS \"SCALE_NODE\",\n    `CHARACTER_SET_NAME` AS \"CHARACTER_NODE\",\n    `COLLATION_NAME` AS \"COLLATION_NODE\",\n    `COLUMN_KEY` AS \"KEY_NODE\",\n    `PRIVILEGES` AS \"PRIVILEGES_NODE\",\n    `COLUMN_COMMENT` AS \"COMMENT_NODE\"\nFROM\n    information_schema.COLUMNS\nWHERE\n    TABLE_SCHEMA = \'${database:String}\'\nAND TABLE_NAME = \'${table:String}\';\n',
        'According to the specified database, the data table gets all the data columns under the current condition\n根据指定数据库，数据表获取当前条件下所有数据列', 'MySQL',
        '[{\"column\":\"database\",\"type\":\"String\",\"expression\":\"${database:String}\"},{\"column\":\"table\",\"type\":\"String\",\"expression\":\"${table:String}\"}]',
        '2023-09-18 23:40:26', '2023-09-18 23:40:26', 1);

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


