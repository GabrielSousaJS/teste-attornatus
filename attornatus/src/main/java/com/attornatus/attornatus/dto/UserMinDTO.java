package com.attornatus.attornatus.dto;

import com.attornatus.attornatus.entities.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

public class UserMinDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "Preencha o campo requerido.")
    private String name;

    @PastOrPresent(message = "A data de nascimento não pode ser futura.")
    private Instant birthDate;

    public UserMinDTO() {
    }

    public UserMinDTO(Long id, String name, Instant birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    public UserMinDTO(User entity) {
        id = entity.getId();
        name = entity.getName();
        birthDate = entity.getBirthDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }
}
