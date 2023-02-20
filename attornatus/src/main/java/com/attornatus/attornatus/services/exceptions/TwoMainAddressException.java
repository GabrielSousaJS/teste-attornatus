package com.attornatus.attornatus.services.exceptions;

import java.io.Serial;

public class TwoMainAddressException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public TwoMainAddressException(String msg) {
        super(msg);
    }
}
