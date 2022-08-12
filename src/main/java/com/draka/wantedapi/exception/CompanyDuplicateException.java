package com.draka.wantedapi.exception;

public class CompanyDuplicateException extends RuntimeException {
    public CompanyDuplicateException(String msg, Throwable t) {
        super(msg, t);
    }

    public CompanyDuplicateException(String msg) {
        super(msg);
    }

    public CompanyDuplicateException() {
        super();
    }
}
