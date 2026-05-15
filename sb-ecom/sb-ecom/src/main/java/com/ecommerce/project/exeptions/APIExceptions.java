package com.ecommerce.project.exeptions;

import java.io.Serial;

public class APIExceptions extends RuntimeException{
    @Serial
    private static final long serialVersionUID=1L;

    public APIExceptions(String message) {
        super(message);
    }

    public APIExceptions() {
    }
}
