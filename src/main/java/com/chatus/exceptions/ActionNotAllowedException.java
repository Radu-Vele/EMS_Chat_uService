package com.chatus.exceptions;

public class ActionNotAllowedException extends Exception {
    public ActionNotAllowedException(String errMessage) {
        super(errMessage);
    }
}
