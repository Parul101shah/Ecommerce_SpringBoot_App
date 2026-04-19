package com.ecommerce.project.exeptions;

public class APIExceptions extends RuntimeException{
    private static final long serialVersionUID=1L;

    public APIExceptions(String message) {
        super(message);
    }

    public APIExceptions() {
    }
}
