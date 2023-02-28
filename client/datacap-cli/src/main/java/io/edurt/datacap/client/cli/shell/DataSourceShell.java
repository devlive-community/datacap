package io.edurt.datacap.client.cli.shell;

import io.edurt.datacap.client.cli.http.HttpConfigure;
import io.edurt.datacap.client.cli.service.CacheService;
import io.edurt.datacap.client.cli.service.CheckService;
import io.edurt.datacap.client.cli.service.DataSourceService;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.table.Table;

@ShellComponent(value = "source")
@ShellCommandGroup(value = "DataSource")
public class DataSourceShell
{
    private final CheckService checkService;
    private final DataSourceService dataSourceService;
    private final CacheService cacheService;

    public DataSourceShell(CheckService checkService, DataSourceService dataSourceService, CacheService cacheService)
    {
        this.checkService = checkService;
        this.dataSourceService = dataSourceService;
        this.cacheService = cacheService;
    }

    @ShellMethod(value = "Get a list of remote server data sources", key = {"source list"})
    @ShellMethodAvailability(value = "availabilityCheck")
    public Table show()
    {
        return this.dataSourceService.showList();
    }

    @ShellMethod(value = "Set the data source flag for subsequent operations on the data source", key = {"source use"})
    @ShellMethodAvailability(value = "availabilityCheck")
    public void setSource(Long id)
    {
        HttpConfigure configure = this.cacheService.getConfigure();
        configure.setSourceId(id);
        System.out.println("Use successful!");
    }

    @ShellMethod(value = "Get data source details", key = {"source info"})
    @ShellMethodAvailability(value = "availabilityCheck")
    public Table getInfo()
    {
        return this.dataSourceService.getInfo();
    }

    public Availability availabilityCheck()
    {
        return this.checkService.availabilityCheck();
    }
}
