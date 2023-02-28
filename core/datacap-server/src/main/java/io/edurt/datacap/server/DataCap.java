package io.edurt.datacap.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class DataCap
{
    public void start(String[] args)
    {
        SpringApplication.run(DataCap.class, args);
    }

    public static void main(String[] args)
    {
        String logbackConfigure = System.getProperty("user.dir") + "/configure/logback.xml";
        if (new File(logbackConfigure).exists()) {
            System.setProperty("logging.config", logbackConfigure);
        }
        new DataCap().start(args);
    }
}
