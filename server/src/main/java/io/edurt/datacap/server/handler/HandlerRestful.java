package io.edurt.datacap.server.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.common.ServiceState;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class HandlerRestful
{
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response<List<HandlerField>> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex)
    {
        List<HandlerField> list = new ArrayList<>();
        BindingResult bindingResult = ex.getBindingResult();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            HandlerField field = new HandlerField();
            field.setField(fieldError.getField());
            field.setMessage(fieldError.getDefaultMessage());
            list.add(field);
        }
        return Response.failure(ServiceState.REQUEST_VALID_ARGUMENT, list);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response<String> handlerConstraintViolationException(ConstraintViolationException ex)
    {
        return Response.failure(ex.getMessage());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response<String> handlerConstraintViolationException(HttpMessageNotReadableException ex)
    {
        if (ex.getCause() instanceof InvalidFormatException) {
            return Response.failure(ServiceState.REQUEST_VALID_ARGUMENT_FORMAT);
        }
        else {
            return Response.failure(ServiceState.REQUEST_VALID_ARGUMENT_LAYOUT);
        }
    }
}
