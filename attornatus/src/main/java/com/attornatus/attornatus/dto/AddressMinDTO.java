package com.attornatus.attornatus.dto;

import com.attornatus.attornatus.entities.Address;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

public class AddressMinDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "Preencha o campo requerido.")
    private String publicPlace;

    @NotBlank(message = "Preencha o campo requerido.")
    @Size(message = "Formato incorreto.", min = 9, max = 9)
    private String cep;

    @Positive(message = "Insira um valor maior do que zero.")
    private Integer number;

    @NotBlank(message = "Preencha o campo requerido.")
    private String city;

    @NotNull(message = "Este campo não pode ser nulo.")
    private Boolean mainAddress;

    public AddressMinDTO() {
    }

    public AddressMinDTO(Long id, String publicPlace, String cep, Integer number, String city, Boolean mainAddress) {
        this.id = id;
        this.publicPlace = publicPlace;
        this.cep = cep;
        this.number = number;
        this.city = city;
        this.mainAddress = mainAddress;
    }

    public AddressMinDTO(Address entity) {
        id = entity.getId();
        publicPlace = entity.getPublicPlace();
        cep = entity.getCep();
        number = entity.getNumber();
        city = entity.getCity();
        mainAddress = entity.getMainAddress();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPublicPlace() {
        return publicPlace;
    }

    public void setPublicPlace(String publicPlace) {
        this.publicPlace = publicPlace;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getMainAddress() {
        return mainAddress;
    }

    public void setMainAddress(Boolean mainAddress) {
        this.mainAddress = mainAddress;
    }
}
