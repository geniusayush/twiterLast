package com.demo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by ayush on 10/02/16.
 */

@ControllerAdvice
@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS})

public class ControllerValidationHandler extends ResponseEntityExceptionHandler {


    private static final Logger logger = LoggerFactory.getLogger(ControllerValidationHandler.class);

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody

    public ExceptionMessage processValidationError(Exception ex) {

        System.out.println("BR Exception");

        String message = ex.getMessage();



        System.out.println(message);
        return new ExceptionMessage(message);
    }
}







