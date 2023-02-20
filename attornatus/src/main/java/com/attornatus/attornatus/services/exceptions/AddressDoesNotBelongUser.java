package com.attornatus.attornatus.services.exceptions;

import java.io.Serial;

public class AddressDoesNotBelongUser extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AddressDoesNotBelongUser(String msg) {
        super(msg);
    }
}
