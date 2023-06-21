package io.edurt.datacap.common.response;

import io.edurt.datacap.common.enums.ServiceState;
import io.edurt.datacap.common.enums.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T>
{
    private Boolean status;
    private Integer code;
    private Object message;
    private T data;

    public static CommonResponse success(Object data)
    {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.code = State.SUCCESS.getCode();
        commonResponse.message = State.SUCCESS.getValue();
        commonResponse.data = data;
        commonResponse.status = true;
        return commonResponse;
    }

    public static CommonResponse failure(String message)
    {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.code = State.FAILURE.getCode();
        commonResponse.message = message;
        commonResponse.status = false;
        return commonResponse;
    }

    public static CommonResponse failure(ServiceState state)
    {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.code = state.getCode();
        commonResponse.message = state.getValue();
        commonResponse.status = false;
        return commonResponse;
    }

    public static CommonResponse failure(ServiceState state, Object message)
    {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.code = state.getCode();
        commonResponse.message = message;
        commonResponse.status = false;
        return commonResponse;
    }
}
