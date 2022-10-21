package com.litts.wormlite.config;

public class EntityConfigException extends RuntimeException{

    public EntityConfigException(String message){
        super(message);
    }

    public EntityConfigException(String message, Throwable cause){
        super(message, cause);
    }

}
