package org.prgrms.awaker.global;

public record ResponseFormat(boolean success, int status, String message, Object body) {
}
