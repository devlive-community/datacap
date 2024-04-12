#
If you are upgrading to 2024.03.3 from a different version, execute the following SQL statement
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

ALTER TABLE `datacap_scheduled`
    ADD COLUMN `code` varchar(100);

RENAME
TABLE `role` TO `datacap_role`;

ALTER TABLE `datacap_role`
    ADD COLUMN `active` BOOLEAN DEFAULT TRUE,
    ADD COLUMN `code` VARCHAR(100),
    ADD COLUMN `update_time` DATETIME;

RENAME
TABLE `role_menu_relation` TO `datacap_role_menu_relation`;

ALTER TABLE `datacap_dashboard`
    ADD COLUMN `code` VARCHAR(100);

ALTER TABLE `datacap_source`
    ADD COLUMN `active` BOOLEAN DEFAULT TRUE;

RENAME
TABLE `snippet` TO `datacap_snippet`;

ALTER TABLE `datacap_report`
    ADD COLUMN `code` VARCHAR(100);

ALTER TABLE `datacap_metadata_database`
    ADD COLUMN `code` VARCHAR(100);

ALTER TABLE `datacap_metadata_table`
    ADD COLUMN `code` VARCHAR(100);

ALTER TABLE `datacap_metadata_column`
    ADD COLUMN `code` VARCHAR(100);

ALTER TABLE `datacap_scheduled_history`
    ADD COLUMN `code` VARCHAR(100);

RENAME
TABLE `menus` TO `datacap_menu`;

RENAME
TABLE `pipeline_user_relation` TO `datacap_pipeline_user_relation`;

RENAME
TABLE `user_roles` TO `datacap_user_role_relation`;

RENAME
TABLE `functions` TO `datacap_function`;

RENAME
TABLE `template_sql` TO `datacap_template`;

RENAME
TABLE `user_log` TO `datacap_user_log`;

ALTER TABLE `datacap_function`
    ADD COLUMN `active` BOOLEAN DEFAULT TRUE,
    ADD COLUMN `code` VARCHAR(100);

ALTER TABLE `datacap_template`
    ADD COLUMN `active` BOOLEAN DEFAULT TRUE,
    ADD COLUMN `code` VARCHAR(100);
