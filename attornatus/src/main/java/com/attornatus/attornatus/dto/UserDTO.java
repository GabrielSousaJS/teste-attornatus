package com.attornatus.attornatus.dto;

import com.attornatus.attornatus.entities.Address;
import com.attornatus.attornatus.entities.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank
    private String name;

    @PastOrPresent(message = "A data de nascimento não pode ser futura.")
    private Instant birthDate;

    private List<AddressDTO> addresses = new ArrayList<>();

    private Set<RoleDTO> roles = new HashSet<>();

    public UserDTO() {
    }

    public UserDTO(Long id, String name, Instant birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    public UserDTO(User entity) {
        id = entity.getId();
        name = entity.getName();
        birthDate = entity.getBirthDate();
        entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
    }

    public UserDTO(User entity, List<Address> addressList) {
        this(entity);
        addressList.forEach(address -> addresses.add(new AddressDTO(address)));
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

    public List<AddressDTO> getAddresses() {
        return addresses;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }
}
