# If you are upgrading to 2024.03.3 from a different version, execute the following SQL statement
# Если вы обновляетесь до версии 2024.03.3 с другой версии, выполните следующую инструкцию SQL
# 如果您是通过其他版本升级到 2024.03.3, 请执行以下 SQL 语句

USE `datacap`;

RENAME
TABLE `audit_plugin` TO `datacap_source_query`;

ALTER TABLE `datacap_source_query`
    CHANGE `end_time` `update_time` datetime;

ALTER TABLE `datacap_source_query`
    ADD `name` VARCHAR(100),
    ADD COLUMN `active` BOOLEAN DEFAULT TRUE,
    ADD COLUMN `code` VARCHAR(100);

UPDATE `datacap_source_query`
SET `code` = REPLACE(UUID(), '-', '')
WHERE `code` IS NULL;

ALTER TABLE `datacap_pipeline`
    ADD COLUMN `active` BOOLEAN DEFAULT TRUE,
    ADD COLUMN `code` VARCHAR(100);

ALTER TABLE `datacap_pipeline`
    CHANGE `start_time` `create_time` DATETIME,
    CHANGE `end_time` `update_time` DATETIME;

UPDATE `datacap_pipeline`
SET `code` = REPLACE(UUID(), '-', '')
WHERE `code` IS NULL;

ALTER TABLE `datacap_scheduled`
    ADD COLUMN `code` varchar(100);

UPDATE `datacap_scheduled`
SET `code` = REPLACE(UUID(), '-', '')
WHERE `code` IS NULL;

RENAME
TABLE `role` TO `datacap_role`;

ALTER TABLE `datacap_role`
    ADD COLUMN `active` BOOLEAN DEFAULT TRUE,
    ADD COLUMN `code` VARCHAR(100),
    ADD COLUMN `update_time` DATETIME;

UPDATE `datacap_role`
SET `code` = REPLACE(UUID(), '-', '')
WHERE `code` IS NULL;

RENAME
TABLE `role_menu_relation` TO `datacap_role_menu_relation`;

ALTER TABLE `datacap_dashboard`
    ADD COLUMN `code` VARCHAR(100);

UPDATE `datacap_dashboard`
SET `code` = REPLACE(UUID(), '-', '')
WHERE `code` IS NULL;

ALTER TABLE `datacap_source`
    ADD COLUMN `active` BOOLEAN DEFAULT TRUE;

UPDATE `datacap_source`
SET `code` = REPLACE(UUID(), '-', '')
WHERE `code` IS NULL;

RENAME
TABLE `snippet` TO `datacap_snippet`;

ALTER TABLE `datacap_snippet`
    CHANGE `code` `context` TEXT;

ALTER TABLE `datacap_snippet`
    ADD COLUMN `code` VARCHAR(100),
    ADD COLUMN `active` BOOLEAN DEFAULT TRUE;

UPDATE `datacap_snippet`
SET `code` = REPLACE(UUID(), '-', '')
WHERE `code` IS NULL;

ALTER TABLE `datacap_report`
    ADD COLUMN `code` VARCHAR(100);

UPDATE `datacap_report`
SET `code` = REPLACE(UUID(), '-', '')
WHERE `code` IS NULL;

ALTER TABLE `datacap_metadata_database`
    ADD COLUMN `code` VARCHAR(100);

UPDATE `datacap_metadata_database`
SET `code` = REPLACE(UUID(), '-', '')
WHERE `code` IS NULL;

ALTER TABLE `datacap_metadata_table`
    ADD COLUMN `code` VARCHAR(100);

UPDATE `datacap_metadata_table`
SET `code` = REPLACE(UUID(), '-', '')
WHERE `code` IS NULL;

ALTER TABLE `datacap_metadata_column`
    ADD COLUMN `code` VARCHAR(100);

UPDATE `datacap_metadata_column`
SET `code` = REPLACE(UUID(), '-', '')
WHERE `code` IS NULL;

ALTER TABLE `datacap_scheduled_history`
    ADD COLUMN `code` VARCHAR(100);

UPDATE `datacap_scheduled_history`
SET `code` = REPLACE(UUID(), '-', '')
WHERE `code` IS NULL;

RENAME
TABLE `menus` TO `datacap_menu`;

UPDATE `datacap_menu`
SET `code` = REPLACE(UUID(), '-', '')
WHERE `code` IS NULL;

RENAME
TABLE `pipeline_user_relation` TO `datacap_pipeline_user_relation`;

RENAME
TABLE `user_roles` TO `datacap_user_role_relation`;

RENAME
TABLE `functions` TO `datacap_function`;

UPDATE `datacap_function`
SET `code` = REPLACE(UUID(), '-', '')
WHERE `code` IS NULL;

RENAME
TABLE `template_sql` TO `datacap_template`;

UPDATE `datacap_template`
SET `code` = REPLACE(UUID(), '-', '')
WHERE `code` IS NULL;

RENAME
TABLE `user_log` TO `datacap_user_log`;

ALTER TABLE `datacap_function`
    ADD COLUMN `active` BOOLEAN DEFAULT TRUE,
    ADD COLUMN `code` VARCHAR(100);

ALTER TABLE `datacap_template`
    ADD COLUMN `active` BOOLEAN DEFAULT TRUE,
    ADD COLUMN `code` VARCHAR(100);

UPDATE `datacap_dataset`
SET `code` = REPLACE(UUID(), '-', '')
WHERE `code` IS NULL;

ALTER TABLE `datacap_dataset_column`
    ADD COLUMN `code` VARCHAR(100);

UPDATE `datacap_dataset_column`
SET `code` = REPLACE(UUID(), '-', '')
WHERE `code` IS NULL;

ALTER TABLE `datacap_dataset_history`
    ADD COLUMN `code` VARCHAR(100);

UPDATE `datacap_dataset_history`
SET `code` = REPLACE(UUID(), '-', '')
WHERE `code` IS NULL;

ALTER TABLE `datacap_source_query`
    ADD COLUMN `home` VARCHAR(500);

ALTER TABLE `datacap_report`
    ADD COLUMN `description` VARCHAR(2000);

ALTER TABLE `datacap_dashboard`
    CHANGE `version` `description` VARCHAR(1000) DEFAULT NULL COMMENT 'Description';

ALTER TABLE `datacap_chat`
    ADD COLUMN `code` VARCHAR(100);

ALTER TABLE `datacap_message`
    ADD COLUMN `code` VARCHAR(100);
