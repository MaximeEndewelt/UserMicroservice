package com.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ExceptionConverter
{
    /**
     * Converts an exception into a HTTP Response
     * @param exception
     * @return
     */
    public static ResponseEntity<?> convertException(Exception exception)
    {
        ResponseEntity output = null;
        if(exception instanceof IllegalArgumentException)
        {
            output = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(exception.getMessage());
        }
        else if(exception instanceof NotFoundException)
        {
            output = ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(exception.getMessage());
        }
        else if(exception instanceof ConflictException)
        {
            output = ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(exception.getMessage());
        }
        else if(exception instanceof UnauthorizedException)
        {
            output = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(exception.getMessage());
        }
        else
        {
            output = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(exception.getMessage());
        }

        return output;
    }
}
