package io.edurt.datacap.service.audit;

import io.edurt.datacap.common.enums.State;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.entity.ExecuteEntity;
import io.edurt.datacap.service.entity.PluginAuditEntity;
import io.edurt.datacap.service.entity.SourceEntity;
import io.edurt.datacap.service.repository.PluginAuditRepository;
import io.edurt.datacap.service.repository.SourceRepository;
import io.edurt.datacap.service.security.UserDetailsService;
import io.edurt.datacap.spi.model.Response;
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
    public void doAfterReturning(JoinPoint joinPoint, AuditPlugin auditPlugin, CommonResponse jsonResult)
    {
        handlerPlugin(joinPoint, pluginAudit, jsonResult);
    }

    protected void handlerPlugin(final JoinPoint joinPoint, final PluginAuditEntity pluginAudit, CommonResponse<Response> jsonResult)
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
                pluginAudit.setMode(executeEntity.getMode());
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
