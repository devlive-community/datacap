create table if not exists source
(
    id          bigint       not null auto_increment,
    _catalog    varchar(255),
    create_time datetime default CURRENT_TIMESTAMP(),
    _database   varchar(255),
    description varchar(255),
    host        varchar(255) not null,
    name        varchar(255) not null,
    password    varchar(255),
    port        bigint       not null,
    protocol    varchar(255),
    username    varchar(255),
    primary key (id)
);
