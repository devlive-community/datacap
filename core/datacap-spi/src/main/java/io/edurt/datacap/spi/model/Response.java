package io.edurt.datacap.spi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Response
{
    private List<String> headers;
    private List<String> types;
    private List<Object> columns;
    private Boolean isConnected = Boolean.FALSE;
    private Boolean isSuccessful = Boolean.FALSE;
    private String message;
    private Time connection;
    private Time processor;
    private String content;
    private Pagination pagination;
}
