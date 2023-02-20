package com.attornatus.attornatus.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class AddressTests {

    @Test
    public void addressShouldHaveCorrectStructure() {

        User user = new User(1L, "John Walker", Instant.parse("1998-12-10T21:00:00Z"), "123456");

        Address entity = new Address();
        entity.setId(1L);
        entity.setPublicPlace("Av. Boa Viagem");
        entity.setCep("51021-000");
        entity.setNumber(1906);
        entity.setCity("Recife");
        entity.setMainAddress(false);
        entity.setUser(user);

        Assertions.assertNotNull(entity.getId());
        Assertions.assertNotNull(entity.getPublicPlace());
        Assertions.assertNotNull(entity.getCep());
        Assertions.assertNotNull(entity.getNumber());
        Assertions.assertNotNull(entity.getCity());
        Assertions.assertNotNull(entity.getMainAddress());
        Assertions.assertNotNull(entity.getUser());

        Assertions.assertNull(entity.getCreatedAt());
        Assertions.assertNull(entity.getUpdatedAt());

    }
}
