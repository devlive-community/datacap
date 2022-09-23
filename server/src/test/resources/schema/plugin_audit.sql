create table if not exists audit_plugin
(
    id          bigint not null auto_increment,
    state       varchar(255),
    create_time datetime default CURRENT_TIMESTAMP(),
    end_time    datetime default CURRENT_TIMESTAMP(),
    plugin_id   bigint not null,
    content     text,
    message     text,
    primary key (id)
);
