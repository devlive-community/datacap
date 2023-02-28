package io.edurt.datacap.client.cli.service;

import org.springframework.shell.Availability;

public interface CheckService
{
    Availability availabilityCheck();
}
