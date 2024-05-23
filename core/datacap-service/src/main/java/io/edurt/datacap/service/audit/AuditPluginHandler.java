package io.edurt.datacap.service.audit;

import com.google.inject.Injector;
import io.edurt.datacap.common.enums.State;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.common.utils.CSVUtils;
import io.edurt.datacap.common.utils.DateUtils;
import io.edurt.datacap.common.utils.SpiUtils;
import io.edurt.datacap.fs.FsRequest;
import io.edurt.datacap.service.common.FolderUtils;
import io.edurt.datacap.service.entity.ExecuteEntity;
import io.edurt.datacap.service.entity.PluginAuditEntity;
import io.edurt.datacap.service.entity.SourceEntity;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.initializer.InitializerConfigure;
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

import java.io.File;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Aspect
@Component
@Slf4j
public class AuditPluginHandler
{
    private final ThreadLocal<PluginAuditEntity> threadLocalPluginAudit = new ThreadLocal<>();

    private final PluginAuditRepository pluginAuditRepository;
    private final SourceRepository sourceRepository;
    private final InitializerConfigure initializer;
    private final Injector injector;

    public AuditPluginHandler(PluginAuditRepository pluginAuditRepository, SourceRepository sourceRepository, InitializerConfigure initializer, Injector injector)
    {
        this.pluginAuditRepository = pluginAuditRepository;
        this.sourceRepository = sourceRepository;
        this.initializer = initializer;
        this.injector = injector;
    }

    @Pointcut("@annotation(auditPlugin)")
    public void cut(AuditPlugin auditPlugin)
    {
    }

    @Before("cut(auditPlugin)")
    public void doBefore(AuditPlugin auditPlugin)
    {
        PluginAuditEntity pluginAudit = new PluginAuditEntity();
        pluginAudit.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        threadLocalPluginAudit.set(pluginAudit);
    }

    @AfterReturning(pointcut = "@annotation(auditPlugin)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, AuditPlugin auditPlugin, CommonResponse jsonResult)
    {
        PluginAuditEntity pluginAudit = threadLocalPluginAudit.get();
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
                pluginAudit.setSource(sourceEntity.get());
            }
            UserEntity user = UserDetailsService.getUser();
            pluginAudit.setUser(user);
            pluginAudit.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
            pluginAudit.setElapsed(pluginAudit.getUpdateTime().getTime() - pluginAudit.getCreateTime().getTime());
            pluginAudit.setCode(UUID.randomUUID().toString().replace("-", ""));
            this.pluginAuditRepository.save(pluginAudit);
            ExecutorService service = Executors.newSingleThreadExecutor();
            service.submit(() -> {
                String uniqueId = !pluginAudit.getCode().isEmpty() ? pluginAudit.getCode() : pluginAudit.getId().toString();
                String workHome = FolderUtils.getWorkHome(initializer.getDataHome(), user.getUsername(), String.join(File.separator, "adhoc", uniqueId));
                log.info("Writer file to folder [ {} ] on [ {} ]", workHome, pluginAudit.getId());
                try {
                    File tempFile = CSVUtils.makeTempCSV(workHome, "result.csv", jsonResult.getData().getHeaders(), jsonResult.getData().getColumns());
                    FsRequest fsRequest = FsRequest.builder()
                            .access(initializer.getFsConfigure().getAccess())
                            .secret(initializer.getFsConfigure().getSecret())
                            .endpoint(workHome)
                            .bucket(initializer.getFsConfigure().getBucket())
                            .stream(Files.newInputStream(tempFile.toPath()))
                            .fileName("result.csv")
                            .build();
                    // If it is OSS third-party storage, rebuild the default directory
                    if (!initializer.getFsConfigure().getType().equals("Local")) {
                        fsRequest.setEndpoint(initializer.getFsConfigure().getEndpoint());
                        fsRequest.setFileName(String.join(File.separator, user.getUsername(), DateUtils.formatYMD(), String.join(File.separator, "adhoc", uniqueId), "result.csv"));
                    }
                    SpiUtils.findFs(injector, initializer.getFsConfigure().getType())
                            .map(v -> v.writer(fsRequest));
                    log.info("Delete temp file [ {} ] on [ {} ] statue [ {} ]", tempFile, pluginAudit.getId(), tempFile.delete());
                    log.info("Writer file to folder [ {} ] on [ {} ] completed", workHome, pluginAudit.getId());
                    pluginAudit.setHome(workHome);
                }
                catch (Exception ex) {
                    log.error("Writer file to folder [ {} ] on [ {} ] failed", workHome, pluginAudit.getId(), ex);
                }
                finally {
                    service.shutdownNow();
                    pluginAuditRepository.save(pluginAudit);
                    threadLocalPluginAudit.remove();
                }
            });
        }
        catch (Exception ex) {
            log.error("Audit failed ", ex);
        }
    }
}
