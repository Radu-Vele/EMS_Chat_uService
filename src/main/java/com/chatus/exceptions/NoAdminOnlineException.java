package com.chatus.exceptions;

public class NoAdminOnlineException extends Exception {
    public NoAdminOnlineException() {
        super("There is no admin online at the moment. Try again later");
    }
}
