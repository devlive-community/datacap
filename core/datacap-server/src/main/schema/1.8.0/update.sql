create table `user_chat`
(
    `id`          int(11)      NOT NULL AUTO_INCREMENT,
    `name`        varchar(255) NOT NULL,
    `question`    text         NOT NULL,
    `answer`      text     DEFAULT NULL,
    `type`        varchar(100),
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `end_time`    datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `elapsed`     bigint,
    `user_id`     int(11)      NOT NULL,
    `is_new`      boolean  default true,
        PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
