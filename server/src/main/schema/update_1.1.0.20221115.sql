ALTER TABLE `datacap`.`source`
    ADD COLUMN `_ssl` boolean default false;

CREATE TABLE IF NOT EXISTS `datacap`.`users`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `username`    varchar(255) DEFAULT NULL COMMENT ' ',
    `password`    varchar(255) DEFAULT NULL COMMENT ' ',
    `create_time` datetime(5)  DEFAULT CURRENT_TIMESTAMP(5),
    PRIMARY KEY (`id`)
) DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `datacap`.`role`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `name`        varchar(255) DEFAULT NULL COMMENT ' ',
    `description` varchar(255) DEFAULT NULL COMMENT ' ',
    `create_time` datetime(5)  DEFAULT CURRENT_TIMESTAMP(5),
    PRIMARY KEY (`id`)
) DEFAULT CHARSET = utf8;

INSERT INTO `datacap`.`role`(`name`, `description`)
VALUES ('Admin', 'Admin role'),
       ('User', 'User role');

CREATE TABLE IF NOT EXISTS `datacap`.`user_roles`
(
    `user_id` bigint(20) NOT NULL,
    `role_id` bigint(20) NOT NULL
)
