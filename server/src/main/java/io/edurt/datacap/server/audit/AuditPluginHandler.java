package io.edurt.datacap.server.audit;

import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.common.State;
import io.edurt.datacap.server.entity.ExecuteEntity;
import io.edurt.datacap.server.entity.PluginAuditEntity;
import io.edurt.datacap.server.entity.SourceEntity;
import io.edurt.datacap.server.repository.PluginAuditRepository;
import io.edurt.datacap.server.repository.SourceRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;

@Aspect
@Component
@Slf4j
public class AuditPluginHandler
{
    private final PluginAuditRepository pluginAuditRepository;
    private final SourceRepository sourceRepository;

    public AuditPluginHandler(PluginAuditRepository pluginAuditRepository, SourceRepository sourceRepository)
    {
        this.pluginAuditRepository = pluginAuditRepository;
        this.sourceRepository = sourceRepository;
    }

    @AfterReturning(pointcut = "@annotation(auditPlugin)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, AuditPlugin auditPlugin, Response jsonResult)
    {
        PluginAuditEntity pluginAudit = new PluginAuditEntity();
        pluginAudit.setCreateTime(Timestamp.from(Instant.now()));
        handlerPlugin(joinPoint, pluginAudit, jsonResult);
    }

    protected void handlerPlugin(final JoinPoint joinPoint, final PluginAuditEntity pluginAudit, Response jsonResult)
    {
        try {
            if (jsonResult.getStatus()) {
                pluginAudit.setState(State.SUCCESS);
            }
            else {
                pluginAudit.setMessage(jsonResult.getMessage().toString());
                pluginAudit.setState(State.FAILURE);
            }
            if (joinPoint.getArgs().length > 0) {
                ExecuteEntity executeEntity = (ExecuteEntity) joinPoint.getArgs()[0];
                pluginAudit.setContent(executeEntity.getContent());
                SourceEntity sourceEntity = this.sourceRepository.findByName(executeEntity.getName());
                pluginAudit.setPlugin(sourceEntity);
            }
            pluginAudit.setEndTime(Timestamp.from(Instant.now()));
            this.pluginAuditRepository.save(pluginAudit);
        }
        catch (Exception ex) {
            log.error("Audit failed ", ex);
        }
    }
}
