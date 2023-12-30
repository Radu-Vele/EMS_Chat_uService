package com.chatus.exceptions;

public class DocumentNotFoundException extends Exception {
    public DocumentNotFoundException() {
        super("Document not found");
    }
}