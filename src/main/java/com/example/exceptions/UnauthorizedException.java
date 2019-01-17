package com.example.exceptions;

public class UnauthorizedException extends RuntimeException
{
    String message;

    public UnauthorizedException(String message)
    {
        this.message= message;
    }
}