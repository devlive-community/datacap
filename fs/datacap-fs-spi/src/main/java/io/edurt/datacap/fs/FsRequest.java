package io.edurt.datacap.fs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FsRequest
{
    private String access;
    private String secret;
    private String endpoint;
    private String bucket;
    private String localPath;
}
