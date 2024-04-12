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
