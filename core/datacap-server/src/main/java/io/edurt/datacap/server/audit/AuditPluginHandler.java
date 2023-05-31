package io.edurt.datacap.server.audit;

import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.common.State;
import io.edurt.datacap.server.entity.ExecuteEntity;
import io.edurt.datacap.server.entity.PluginAuditEntity;
import io.edurt.datacap.server.entity.SourceEntity;
import io.edurt.datacap.server.repository.PluginAuditRepository;
import io.edurt.datacap.server.repository.SourceRepository;
import io.edurt.datacap.server.security.UserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Aspect
@Component
@Slf4j
public class AuditPluginHandler
{
    private final PluginAuditRepository pluginAuditRepository;
    private final SourceRepository sourceRepository;
    private PluginAuditEntity pluginAudit;

    public AuditPluginHandler(PluginAuditRepository pluginAuditRepository, SourceRepository sourceRepository)
    {
        this.pluginAuditRepository = pluginAuditRepository;
        this.sourceRepository = sourceRepository;
    }

    @Pointcut("@annotation(auditPlugin)")
    public void cut(AuditPlugin auditPlugin)
    {
    }

    @Before("cut(auditPlugin)")
    public void doBefore(AuditPlugin auditPlugin)
    {
        this.pluginAudit = new PluginAuditEntity();
        pluginAudit.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
    }

    @AfterReturning(pointcut = "@annotation(auditPlugin)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, AuditPlugin auditPlugin, Response jsonResult)
    {
        handlerPlugin(joinPoint, pluginAudit, jsonResult);
    }

    protected void handlerPlugin(final JoinPoint joinPoint, final PluginAuditEntity pluginAudit, Response<io.edurt.datacap.spi.model.Response> jsonResult)
    {
        try {
            if (jsonResult.getStatus()) {
                pluginAudit.setState(State.SUCCESS);
                pluginAudit.setCount(jsonResult.getData().getColumns().size());
            }
            else {
                pluginAudit.setMessage(jsonResult.getMessage().toString());
                pluginAudit.setState(State.FAILURE);
            }
            if (joinPoint.getArgs().length > 0) {
                ExecuteEntity executeEntity = (ExecuteEntity) joinPoint.getArgs()[0];
                pluginAudit.setContent(executeEntity.getContent());
                Optional<SourceEntity> sourceEntity = this.sourceRepository.findById(Long.valueOf(executeEntity.getName()));
                pluginAudit.setPlugin(sourceEntity.get());
            }
            pluginAudit.setUser(UserDetailsService.getUser());
            pluginAudit.setEndTime(Timestamp.valueOf(LocalDateTime.now()));
            this.pluginAuditRepository.save(pluginAudit);
        }
        catch (Exception ex) {
            log.error("Audit failed ", ex);
        }
    }
}
