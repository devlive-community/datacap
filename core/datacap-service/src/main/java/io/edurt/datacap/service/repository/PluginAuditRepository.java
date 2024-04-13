package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.activity.HeatmapActivity;
import io.edurt.datacap.service.entity.PluginAuditEntity;
import io.edurt.datacap.service.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface PluginAuditRepository
        extends BaseRepository<PluginAuditEntity, Long>
{
    Page<PluginAuditEntity> findAllByUser(UserEntity user, Pageable pageable);

    Long countByUser(UserEntity user);

    @Query(value = "SELECT new io.edurt.datacap.service.activity.HeatmapActivity(COUNT(1), FUNCTION('DATE', p.createTime)) " +
            "FROM PluginAuditEntity AS p " +
            "WHERE p.user = :user " +
            "GROUP BY FUNCTION('DATE', p.createTime) " +
            "ORDER BY FUNCTION('DATE', p.createTime) ASC")
    List<HeatmapActivity> countByCreateTimeAndFindByUser(@Param(value = "user") UserEntity user);

    @Query(value = "SELECT count(1) AS dataOfCount, s.name AS dataOfLabel\n" +
            "FROM datacap_source_query AS ap\n" +
            "LEFT JOIN datacap_source AS s ON ap.plugin_id = s.id\n" +
            "WHERE date_sub(curdate(), interval 7 day) <= date(ap.create_time) AND s.user_id = ?1\n" +
            "GROUP BY s.name", nativeQuery = true)
    List<Map<String, Object>> selectRadarByUserId(Long id);
}
