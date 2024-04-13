USE `datacap`;

ALTER TABLE `pipeline`
    ADD COLUMN `executor` VARCHAR(20) NOT NULL DEFAULT 'Seatunnel';

RENAME TABLE `pipeline` TO `datacap_pipeline`;

ALTER TABLE `datacap_pipeline`
    DROP COLUMN `user_id`;

CREATE TABLE `pipeline_user_relation`
(
    `pipeline_id` INT,
    `user_id`     INT
);
