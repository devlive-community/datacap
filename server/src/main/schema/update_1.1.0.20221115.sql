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

INSERT INTO `datacap`.`users` (`id`, `username`, `password`)
VALUES (1, 'admin', '$2a$10$dNGjVHzGLnI7MA50iiD8V.LFPPYetB/04eTiZm8ULaso/BaKV.RS.'),
       (2, 'datacap', '$2a$10$o3h5jWxwNzxkWyP4wPXz4eoduNkQpF7eaCLStw2VYlTU2BUbed0Di');

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
);

INSERT INTO `datacap`.`user_roles` (`user_id`, `role_id`)
VALUES (1, 1),
       (2, 2);

ALTER TABLE `datacap`.`source`
    ADD COLUMN `public` boolean default false;
-- Set the openness of the previous version to public
UPDATE `datacap`.`source`
SET `public` = true;

ALTER TABLE `datacap`.`source`
    ADD COLUMN `user_id` bigint(20) NOT NULL;
-- Example Set the user of the previous version as the administrator
UPDATE `datacap`.`source`
SET `user_id` = 1;

ALTER TABLE `datacap`.`audit_plugin`
    ADD COLUMN `user_id` bigint(20) NOT NULL;
-- Example Set the user of the previous version as the administrator
UPDATE `datacap`.`audit_plugin`
SET `user_id` = 1;

CREATE TABLE IF NOT EXISTS `datacap`.`snippet`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `name`        varchar(255) DEFAULT NULL COMMENT ' ',
    `description` varchar(255) DEFAULT NULL COMMENT ' ',
    `code`        text         DEFAULT NULL COMMENT ' ',
    `create_time` timestamp    DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp    DEFAULT CURRENT_TIMESTAMP,
    `user_id`     bigint(20) NOT NULL,
    PRIMARY KEY (`id`)
) DEFAULT CHARSET = utf8;

ALTER TABLE `datacap`.`source`
    ADD COLUMN `configure` text;
