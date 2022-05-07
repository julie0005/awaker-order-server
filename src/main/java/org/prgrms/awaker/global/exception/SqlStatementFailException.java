package org.prgrms.awaker.global.exception;

public class SqlStatementFailException extends RuntimeException {
    public SqlStatementFailException(String message) {
        super(message);
    }
}
