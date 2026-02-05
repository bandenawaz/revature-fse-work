package org.example.exception;

public class InternalSystemException extends Exception{
    public InternalSystemException(String message, Throwable cause){
        super(message,cause);
    }
}
