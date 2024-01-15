# If you are upgrading to 2024.01.1 from a different version, execute the following SQL statement
# Если вы обновляетесь до версии 2024.01.1 с другой версии, выполните следующую инструкцию SQL
# 如果您是通过其他版本升级到 2024.01.1, 请执行以下 SQL 语句

ALTER TABLE `datacap_dashboard`
    ADD COLUMN `version` VARCHAR(100) COMMENT 'Dashboard version' DEFAULT 'V2';

UPDATE `datacap_dashboard`
SET `version` = 'V1'
WHERE `version` IS NULL;

ALTER TABLE `audit_plugin`
    ADD COLUMN `query_mode` VARCHAR(100) DEFAULT 'ADHOC';

UPDATE `audit_plugin`
SET `query_mode` = 'ADHOC'
WHERE `query_mode` IS NULL;

ALTER TABLE `datacap_dataset`
    ADD COLUMN `code` VARCHAR(100) default (uuid());

ALTER TABLE `datacap_dataset_column`
    ADD COLUMN `column_mode` VARCHAR(100) default 'DIMENSION';

UPDATE `menus`
SET `is_new` = false
WHERE `is_new` = true;

CREATE TABLE `datacap_report_dataset_relation`
(
    `report_id`  BIGINT,
    `dataset_id` BIGINT
);

UPDATE `template_sql`
SET `plugin` = 'MySQL,MatrixOne'
WHERE `plugin` = 'MySQL';

CREATE TABLE `datacap_scheduled_history_source_relation`
(
    `scheduled_history_id` BIGINT,
    `source_id`            BIGINT
);

RENAME TABLE `source` TO `datacap_source`;

ALTER TABLE `datacap_dataset`
    CHANGE `sync_value` `expression` VARCHAR(100) NULL;

ALTER TABLE `datacap_dataset`
    ADD COLUMN `actuator` VARCHAR(100) NULL;

ALTER TABLE `datacap_dataset_column`
    ADD COLUMN `is_partition_key` BOOLEAN DEFAULT FALSE;
