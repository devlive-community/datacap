package io.edurt.datacap.fs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Reader;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FsResponse
{
    private String origin;
    private String remote;
    private String message;
    private Reader context;
    private boolean successful;
}
