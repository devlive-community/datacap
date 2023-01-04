USE `datacap`;

UPDATE `template_sql`
SET `system` = true
WHERE `name` = 'getAllDatabaseAndTable';

INSERT INTO `template_sql` (`name`, `content`, `description`, `plugin`, `configure`, `create_time`, `update_time`, `system`)
VALUES ('getAllRunningProcess',
        'SELECT\n  query_id AS id,\n  query AS content,\n  toUInt64(toUInt64(read_rows) + toUInt64(written_rows)) AS rows,\n  round(elapsed, 1) AS elapsed,\n  formatReadableSize(toUInt64(read_bytes) + toUInt64(written_bytes)) AS bytes,\n  formatReadableSize(memory_usage) AS memory,\n  formatReadableSize(read_bytes) AS bytesRead,\n  formatReadableSize(written_bytes) AS bytesWritten\nFROM\n  system.processes\nWHERE\n  round(elapsed, 1) > 0',
        'Get a running list of all currently specified server nodes', 'ClickHouse', '[]', '2022-12-29 16:40:11', '2022-12-29 16:40:11', true);
INSERT INTO `template_sql` (`name`, `content`, `description`, `plugin`, `configure`, `create_time`, `update_time`, `system`)
VALUES ('getAllRunningProcess',
        'SELECT\n  id AS id,\n  info AS content,\n  \'-\' AS rows,\n  time AS elapsed,\n  \'-\' AS bytes,\n  \'-\' AS memory,\n  \'-\' AS bytesRead,\n  \'-\' AS bytesWritten\nFROM\n  information_schema.PROCESSLIST',
        'Get a running list of all currently specified server nodes', 'MySQL', '[]', '2022-12-29 19:33:04', '2022-12-29 19:33:04', true);