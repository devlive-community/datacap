USE `datacap`;

ALTER TABLE `source`
    ADD COLUMN `version`     VARCHAR(255) DEFAULT NULL,
    ADD COLUMN `available`   BOOLEAN      DEFAULT TRUE,
    ADD COLUMN `message`     TEXT         DEFAULT NULL,
    ADD COLUMN `update_time` DATETIME     DEFAULT NULL;
