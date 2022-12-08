USE datacap;

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
  DEFAULT CHARSET = utf8;

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
  DEFAULT CHARSET = utf8;

INSERT INTO `datacap`.`users` (`id`, `username`, `password`)
VALUES (1, 'admin', '$2a$10$dNGjVHzGLnI7MA50iiD8V.LFPPYetB/04eTiZm8ULaso/BaKV.RS.'),
       (2, 'datacap', '$2a$10$o3h5jWxwNzxkWyP4wPXz4eoduNkQpF7eaCLStw2VYlTU2BUbed0Di');

INSERT INTO `datacap`.`role`(`name`, `description`)
VALUES ('Admin', 'Admin role'),
       ('User', 'User role');

INSERT INTO `datacap`.`user_roles` (`user_id`, `role_id`)
VALUES (1, 1),
       (2, 2);

SET FOREIGN_KEY_CHECKS = 1;
