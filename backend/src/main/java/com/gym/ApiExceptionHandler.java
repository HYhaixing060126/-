package com.gym;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,Object> badRequest(Exception e){Map<String,Object> r=new HashMap<>();r.put("success",false);r.put("message",e.getMessage());return r;}
}
