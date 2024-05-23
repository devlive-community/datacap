package io.edurt.datacap.common.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class SignResponse
{
    private Long timestamp;
    private String sign;
}
