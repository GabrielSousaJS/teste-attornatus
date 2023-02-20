package com.attornatus.attornatus.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class UserTests {

    @Test
    public void userShouldHaveCorrectStructure() {

        User entity = new User();
        entity.setId(1L);
        entity.setName("John Walker");
        entity.setBirthDate(Instant.parse("1998-12-10T21:00:00Z"));
        entity.setPassword("123456");

        Assertions.assertNotNull(entity.getId());
        Assertions.assertNotNull(entity.getName());
        Assertions.assertNotNull(entity.getPassword());

        Assertions.assertEquals(0, entity.getAddresses().size());

    }
}
