package io.edurt.datacap.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataCap
{
    public void start(String[] args)
    {
        SpringApplication.run(DataCap.class, args);
    }

    public static void main(String[] args)
    {
        new DataCap().start(args);
    }
}
