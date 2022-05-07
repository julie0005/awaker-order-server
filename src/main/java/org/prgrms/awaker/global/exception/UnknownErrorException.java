package org.prgrms.awaker.global.exception;

public class UnknownErrorException extends RuntimeException{
    public UnknownErrorException(String message){
        super(message);
    }
}
