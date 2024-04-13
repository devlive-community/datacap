CREATE TABLE `pipeline`
(
    `id`              int(11)      NOT NULL AUTO_INCREMENT,
    `name`            varchar(255) NOT NULL,
    `content`         text         NOT NULL,
    `state`           varchar(100) DEFAULT NULL,
    `message`         text,
    `work`            text,
    `start_time`      datetime     DEFAULT CURRENT_TIMESTAMP,
    `end_time`        datetime     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `elapsed`         bigint,
    `user_id`         int(11)      NOT NULL,
    `from_id`         int(11)      NOT NULL,
    `from_configures` text,
    `to_id`           int(11)      NOT NULL,
    `to_configures`   text,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;