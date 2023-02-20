package com.attornatus.attornatus.dto;

import com.attornatus.attornatus.services.validation.UserInsertValid;

import javax.validation.constraints.NotBlank;

@UserInsertValid
public class UserInsertDTO extends UserDTO {

    @NotBlank(message = "Preecha o campo requerido")
    private String password;

    public UserInsertDTO() {
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
