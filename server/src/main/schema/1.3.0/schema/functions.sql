USE datacap;

DROP TABLE IF EXISTS `functions`;
CREATE TABLE `functions`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `name`        varchar(255)        DEFAULT NULL COMMENT 'Function name',
    `content`     varchar(255)        DEFAULT NULL COMMENT 'Expression of function',
    `description` text                DEFAULT NULL COMMENT 'Function description',
    `plugin`      varchar(255)        DEFAULT NULL COMMENT 'Trial plug-in, multiple according to, split',
    `example`     text                DEFAULT NULL COMMENT 'Function Usage Example',
    `type`        VARCHAR(20)         DEFAULT 'KEYWORDS' COMMENT 'type',
    `create_time` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT 'Plug-in correlation function';
