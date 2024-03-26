package com.sky.handler;

public class BusinessException extends RuntimeException{
    private final int code;
    private final String description;

    public BusinessException(String message,int code,String description){
        super(message);
        this.code = code;
        this.description =description;
    }





}
