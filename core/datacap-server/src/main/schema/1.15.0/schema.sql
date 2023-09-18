USE `datacap`;

ALTER TABLE `template_sql`
    CHANGE COLUMN `system` `is_system` BOOLEAN;

CREATE TABLE `datacap_metadata_database`
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(255),
    active      TINYINT(1) DEFAULT 1,
    create_time DATETIME,
    update_time DATETIME,
    description VARCHAR(1000),
    catalog     VARCHAR(255)
);

CREATE TABLE `datacap_metadata_database_source_relation`
(
    source_id   INT,
    database_id INT
);

CREATE TABLE `datacap_metadata_table`
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(255),
    active      TINYINT(1) DEFAULT 1,
    create_time DATETIME,
    update_time DATETIME,
    type        VARCHAR(255),
    description VARCHAR(1000)
);

CREATE TABLE `datacap_metadata_table_database_relation`
(
    table_id    INT,
    database_id INT
);

CREATE TABLE `datacap_metadata_column`
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(255),
    active      TINYINT(1) DEFAULT 1,
    create_time DATETIME,
    update_time DATETIME,
    description VARCHAR(1000),
    type        VARCHAR(255),
    length      INTEGER,
    is_primary  TINYINT(1) DEFAULT FALSE,
    is_index    TINYINT(1) DEFAULT FALSE
);

CREATE TABLE `datacap_metadata_column_table_relation`
(
    table_id  INT,
    column_id INT
);
