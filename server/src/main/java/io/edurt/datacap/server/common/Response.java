package io.edurt.datacap.server.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Response<T>
{
    private Boolean status;
    private Integer code;
    private String message;
    private T data;

    public static Response success(Object data)
    {
        Response response = new Response();
        response.code = State.SUCCESS.getCode();
        response.message = State.SUCCESS.getValue();
        response.data = data;
        response.status = true;
        return response;
    }

    public static Response failure(String message)
    {
        Response response = new Response();
        response.code = State.FAILURE.getCode();
        response.message = message;
        response.status = false;
        return response;
    }

    public static Response failure(ServiceState state)
    {
        Response response = new Response();
        response.code = state.getCode();
        response.message = state.getValue();
        response.status = false;
        return response;
    }
}
