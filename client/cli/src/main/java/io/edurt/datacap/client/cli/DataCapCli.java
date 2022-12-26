package io.edurt.datacap.client.cli;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataCapCli
{
    public void start(String[] args)
    {
        SpringApplication application = new SpringApplication(DataCapCli.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.setLogStartupInfo(false);
        application.run(args);
    }

    public static void main(String[] args)
    {
        new DataCapCli().start(args);
    }
}
