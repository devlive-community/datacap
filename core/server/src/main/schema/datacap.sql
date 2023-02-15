SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for audit_plugin
-- ----------------------------
DROP TABLE IF EXISTS `audit_plugin`;
CREATE TABLE `audit_plugin`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `state`       varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `create_time` mediumtext COLLATE utf8_bin,
    `end_time`    mediumtext COLLATE utf8_bin,
    `plugin_id`   bigint(20) NOT NULL,
    `content`     text COLLATE utf8_bin,
    `message`     text COLLATE utf8_bin,
    `elapsed`     bigint(20)                    DEFAULT '0',
    `user_id`     bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    FULLTEXT KEY `full_text_index_for_content` (`content`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

-- ----------------------------
-- Records of audit_plugin
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for functions
-- ----------------------------
DROP TABLE IF EXISTS `functions`;
CREATE TABLE `functions`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `name`        varchar(255)        DEFAULT NULL COMMENT 'Function name',
    `content`     varchar(255)        DEFAULT NULL COMMENT 'Expression of function',
    `description` text COMMENT 'Function description',
    `plugin`      varchar(255)        DEFAULT NULL COMMENT 'Trial plug-in, multiple according to, split',
    `example`     text COMMENT 'Function Usage Example',
    `create_time` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `type`        varchar(20)         DEFAULT 'KEYWORDS',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='Plug-in correlation function';

-- ----------------------------
-- Records of functions
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `name`        varchar(255) DEFAULT NULL COMMENT ' ',
    `description` varchar(255) DEFAULT NULL COMMENT ' ',
    `create_time` datetime(5)  DEFAULT CURRENT_TIMESTAMP(5),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
BEGIN;
INSERT INTO `role` (`id`, `name`, `description`, `create_time`)
VALUES (1, 'Admin', 'Admin role', '2022-10-19 13:45:06.83388');
INSERT INTO `role` (`id`, `name`, `description`, `create_time`)
VALUES (2, 'User', 'User role', '2022-10-19 13:45:06.83388');
COMMIT;

-- ----------------------------
-- Table structure for snippet
-- ----------------------------
DROP TABLE IF EXISTS `snippet`;
CREATE TABLE `snippet`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `name`        varchar(255)        DEFAULT NULL COMMENT ' ',
    `description` varchar(255)        DEFAULT NULL COMMENT ' ',
    `code`        text COMMENT ' ',
    `create_time` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_id`     bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    FULLTEXT KEY `full_text_index_for_code` (`code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of snippet
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for source
-- ----------------------------
DROP TABLE IF EXISTS `source`;
CREATE TABLE `source`
(
    `id`          bigint(20)                    NOT NULL AUTO_INCREMENT,
    `_catalog`    varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `create_time` datetime                      DEFAULT CURRENT_TIMESTAMP,
    `_database`   varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `description` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `host`        varchar(255) COLLATE utf8_bin NOT NULL,
    `name`        varchar(255) COLLATE utf8_bin NOT NULL,
    `password`    varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `port`        bigint(20)                    NOT NULL,
    `protocol`    varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `username`    varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `_type`       varchar(100) COLLATE utf8_bin NOT NULL,
    `ssl`         tinyint(1)                    DEFAULT '0',
    `_ssl`        tinyint(1)                    DEFAULT '0',
    `publish`     tinyint(1)                    DEFAULT '0',
    `public`      tinyint(1)                    DEFAULT '0',
    `user_id`     bigint(20)                    DEFAULT NULL,
    `configure`   text COLLATE utf8_bin,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin COMMENT ='The storage is used to query the data connection source';

-- ----------------------------
-- Records of source
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for template_sql
-- ----------------------------
DROP TABLE IF EXISTS `template_sql`;
CREATE TABLE `template_sql`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `name`        varchar(255)        DEFAULT NULL COMMENT 'Name of template',
    `content`     text,
    `description` text,
    `plugin`      varchar(50)         DEFAULT NULL COMMENT 'Using plug-ins',
    `configure`   text COMMENT 'The template must use the configuration information in the key->value format',
    `create_time` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `system`      tinyint(1)          DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 17
  DEFAULT CHARSET = utf8 COMMENT ='The system preset SQL template table';

-- ----------------------------
-- Records of template_sql
-- ----------------------------
BEGIN;
INSERT INTO `template_sql` (`id`, `name`, `content`, `description`, `plugin`, `configure`, `create_time`, `update_time`, `system`)
VALUES (1, 'getAllDatabase', 'SHOW DATABASES', 'Gets a list of all databases', 'ClickHouse,MySQL', '[]', '2022-12-08 18:38:39', '2022-12-08 18:38:39', 0);
INSERT INTO `template_sql` (`id`, `name`, `content`, `description`, `plugin`, `configure`, `create_time`, `update_time`, `system`)
VALUES (2, 'getAllTablesFromDatabase', 'SHOW TABLES FROM ${database:String}', 'Get the data table from the database', 'ClickHouse,MySQL',
        '[{\"column\":\"database\",\"type\":\"String\",\"expression\":\"${database:String}\"}]', '2022-12-08 19:25:31', '2022-12-08 19:25:31', 0);
INSERT INTO `template_sql` (`id`, `name`, `content`, `description`, `plugin`, `configure`, `create_time`, `update_time`, `system`)
VALUES (3, 'getAllDatabaseAndTable', 'SELECT database as tableSchema, name as tableName\nFROM system.tables\nWHERE database NOT IN (\'system\')\nORDER BY tableSchema, tableName',
        'Get all databases (including all tables)', 'ClickHouse', '[]', '2022-12-08 22:52:59', '2022-12-08 22:52:59', 1);
INSERT INTO `template_sql` (`id`, `name`, `content`, `description`, `plugin`, `configure`, `create_time`, `update_time`, `system`)
VALUES (4, 'getAllDatabaseAndTable', 'SELECT t.table_schema AS tableSchema, t.table_name AS tableName\nFROM INFORMATION_SCHEMA.TABLES t\nORDER BY t.table_schema, t.table_name',
        'Get all databases (including all tables)', 'MySQL', '[]', '2022-12-08 23:04:45', '2022-12-08 23:04:45', 1);
INSERT INTO `template_sql` (`id`, `name`, `content`, `description`, `plugin`, `configure`, `create_time`, `update_time`, `system`)
VALUES (5, 'getAllDatabaseAndTables', 'show tables', 'show tables', 'ClickHouse', '[]', '2022-12-27 18:51:02', '2022-12-27 18:51:02', 0);
INSERT INTO `template_sql` (`id`, `name`, `content`, `description`, `plugin`, `configure`, `create_time`, `update_time`, `system`)
VALUES (7, 'getAllRunningProcess',
        'SELECT\n  query_id AS id,\n  query AS content,\n  toUInt64(toUInt64(read_rows) + toUInt64(written_rows)) AS rows,\n  round(elapsed, 1) AS elapsed,\n  formatReadableSize(toUInt64(read_bytes) + toUInt64(written_bytes)) AS bytes,\n  formatReadableSize(memory_usage) AS memory,\n  formatReadableSize(read_bytes) AS bytesRead,\n  formatReadableSize(written_bytes) AS bytesWritten\nFROM\n  system.processes\nWHERE\n  round(elapsed, 1) > 0',
        'Get a running list of all currently specified server nodes', 'ClickHouse', '[]', '2022-12-29 16:40:11', '2022-12-29 16:40:11', 0);
INSERT INTO `template_sql` (`id`, `name`, `content`, `description`, `plugin`, `configure`, `create_time`, `update_time`, `system`)
VALUES (8, 'getAllRunningProcess',
        'SELECT\n  id AS id,\n  info AS content,\n  \'-\' AS rows,\n  time AS elapsed,\n  \'-\' AS bytes,\n  \'-\' AS memory,\n  \'-\' AS bytesRead,\n  \'-\' AS bytesWritten\nFROM\n  information_schema.PROCESSLIST',
        'Get a running list of all currently specified server nodes', 'MySQL', '[]', '2022-12-29 19:33:04', '2022-12-29 19:33:04', 0);
INSERT INTO `template_sql` (`id`, `name`, `content`, `description`, `plugin`, `configure`, `create_time`, `update_time`, `system`)
VALUES (10, 'getAllColumnsFromDatabaseAndTable', 'DESC ${table:String}', 'Get the data column from the database and table', 'MySQL,ClickHouse',
        '[{\"column\":\"table\",\"type\":\"String\",\"expression\":\"${table:String}\"}]', '2023-01-10 11:59:23', '2023-01-10 11:59:23', 0);
INSERT INTO `template_sql` (`id`, `name`, `content`, `description`, `plugin`, `configure`, `create_time`, `update_time`, `system`)
VALUES (11, 'getDataFromDatabaseAndTableLimited', 'SELECT *\nFROM ${table:String}\nLIMIT ${size:Integer}\nOFFSET ${page:Integer}', 'Get all data from table by limited',
        'MySQL,ClickHouse',
        '[{\"column\":\"table\",\"type\":\"String\",\"expression\":\"${table:String}\"},{\"column\":\"size\",\"type\":\"Integer\",\"expression\":\"${size:Integer}\"},{\"column\":\"page\",\"type\":\"Integer\",\"expression\":\"${page:Integer}\"}]',
        '2023-01-10 13:31:10', '2023-01-10 13:31:10', 0);
COMMIT;

-- ----------------------------
-- Table structure for user_log
-- ----------------------------
DROP TABLE IF EXISTS `user_log`;
CREATE TABLE `user_log`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `device`      varchar(255) DEFAULT NULL COMMENT 'Login device',
    `client`      varchar(255) DEFAULT NULL COMMENT 'Login client',
    `ip`          varchar(100) DEFAULT NULL COMMENT 'Login ip',
    `message`     varchar(225) DEFAULT NULL COMMENT 'Error message',
    `state`       varchar(20)  DEFAULT NULL COMMENT 'Login state',
    `ua`          varchar(255) DEFAULT NULL COMMENT 'Trial plug-in, multiple according to, split',
    `user_id`     bigint(20) NOT NULL,
    `create_time` datetime(5)  DEFAULT CURRENT_TIMESTAMP(5),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='User login log';

-- ----------------------------
-- Records of user_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for user_roles
-- ----------------------------
DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles`
(
    `user_id` bigint(20) NOT NULL,
    `role_id` bigint(20) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

-- ----------------------------
-- Records of user_roles
-- ----------------------------
BEGIN;
INSERT INTO `user_roles` (`user_id`, `role_id`)
VALUES (1, 1);
INSERT INTO `user_roles` (`user_id`, `role_id`)
VALUES (2, 2);
COMMIT;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `username`    varchar(255) DEFAULT NULL COMMENT ' ',
    `password`    varchar(255) DEFAULT NULL COMMENT ' ',
    `create_time` datetime(5)  DEFAULT CURRENT_TIMESTAMP(5),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 10
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` (`id`, `username`, `password`, `create_time`)
VALUES (1, 'admin', '$2a$10$ee2yg.Te14GpHppDUROAi.HzYR5Q.q2/5vrZvAr4TFY3J2iT663JG', NULL);
INSERT INTO `users` (`id`, `username`, `password`, `create_time`)
VALUES (2, 'datacap', '$2a$10$bZ4XBRlYUjKfkBovWT9TuuXlEF7lpRxVrXS8iqyCjCHUqy4RPTL8.', NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
