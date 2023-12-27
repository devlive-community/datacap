# If you are upgrading to 2024.01.1 from a different version, execute the following SQL statement
# Если вы обновляетесь до версии 2024.01.1 с другой версии, выполните следующую инструкцию SQL
# 如果您是通过其他版本升级到 2024.01.1, 请执行以下 SQL 语句

ALTER TABLE `datacap_dashboard`
    ADD COLUMN `version` VARCHAR(100) COMMENT 'Dashboard version' DEFAULT 'V2';

UPDATE `datacap_dashboard`
SET `version` = 'V1'
WHERE `version` IS NULL;
