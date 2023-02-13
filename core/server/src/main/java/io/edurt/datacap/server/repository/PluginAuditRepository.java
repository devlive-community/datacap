package io.edurt.datacap.server.repository;

import io.edurt.datacap.server.entity.PluginAuditEntity;
import io.edurt.datacap.server.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Map;

public interface PluginAuditRepository
        extends PagingAndSortingRepository<PluginAuditEntity, Long>
{
    Page<PluginAuditEntity> findAllByUser(UserEntity user, Pageable pageable);

    Long countByUser(UserEntity user);

    @Query(value = "SELECT tbl._date AS dataOfDate," +
            "   IFNULL(tbr.totalnum, 0) AS dataOfCount," +
            "   lastDay AS dataOfLastDay," +
            "   lastWeek AS dataOfLastWeek," +
            "   `month` AS dataOfMonth," +
            "   `week` AS dataOfWeek," +
            "   DAYOFWEEK(tbl._date) - 1 AS dataOfDay\n" +
            "FROM\n" +
            "  (\n" +
            "    SELECT\n" +
            "        @s \\:= @s + 1 AS _index,\n" +
            "        DATE(DATE_SUB(DATE(CONCAT(YEAR(now()),'-12-31')), INTERVAL @s DAY)) AS _date,\n" +
            "        if(DATE(DATE_SUB(DATE(CONCAT(YEAR(now()),'-12-31')), INTERVAL @s DAY)) = last_day(DATE(DATE_SUB(DATE(CONCAT(YEAR(now()),'-12-31')), INTERVAL @s DAY))), 'true', 'false') AS lastDay,\n" +
            "        if(last_day(DATE(DATE_SUB(DATE(CONCAT(YEAR(now()),'-12-31')), INTERVAL @s DAY))) - DATE(DATE_SUB(DATE(CONCAT(YEAR(now()),'-12-31')), INTERVAL @s DAY)) < 7, 'true', 'false') AS lastWeek,\n" +
            "        month(DATE(DATE_SUB(DATE(CONCAT(YEAR(now()),'-12-31')), INTERVAL @s DAY))) AS `month`,\n" +
            "        week(DATE(DATE_SUB(DATE(CONCAT(YEAR(now()),'-12-31')), INTERVAL @s DAY))) AS `week`,\n" +
            "        day(DATE(DATE_SUB(DATE(CONCAT(YEAR(now()),'-12-31')), INTERVAL @s DAY))) AS `day`\n" +
            "    FROM\n" +
            "        information_schema.tables, (SELECT @s \\:= - 1) temp\n" +
            "    WHERE @s < 365\n" +
            "    ORDER BY _date\n" +
            "  ) AS tbl\n" +
            "  LEFT JOIN (\n" +
            "    SELECT\n" +
            "        count(1) AS totalnum,\n" +
            "        DATE(create_time) create_time\n" +
            "    FROM audit_plugin\n" +
            "    WHERE user_id = ?1\n" +
            "    GROUP BY DATE(create_time)\n" +
            "  ) AS tbr ON tbl._date = tbr.create_time\n" +
            "GROUP BY tbl._date", nativeQuery = true)
    List<Map<String, Object>> selectContributionByUserId(Long id);

    @Query(value = "SELECT count(1) AS dataOfCount, s.name AS dataOfLabel\n" +
            "FROM audit_plugin AS ap\n" +
            "LEFT JOIN source AS s ON ap.plugin_id = s.id\n" +
            "WHERE date_sub(curdate(), interval 7 day) <= date(ap.create_time) AND s.user_id = ?1\n" +
            "GROUP BY s.name", nativeQuery = true)
    List<Map<String, Object>> selectRadarByUserId(Long id);
}
