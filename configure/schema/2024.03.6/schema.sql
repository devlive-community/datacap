USE `datacap`;

ALTER TABLE `datacap_source_query`
    ADD COLUMN `format` VARCHAR(100) DEFAULT 'Json';
