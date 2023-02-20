package com.attornatus.attornatus.resources;


import com.attornatus.attornatus.dto.UserInsertDTO;
import com.attornatus.attornatus.dto.UserMinDTO;
import com.attornatus.attornatus.dto.UserUpdateDTO;
import com.attornatus.attornatus.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingIdAdmin;
    private Long existingIdClient;
    private Long nonExistingId;
    private Long countTotalElements;

    private String adminUsername;
    private String adminPassword;
    private String clientUsername;
    private String clientPassword;

    @BeforeEach
    void setUp() throws Exception {
        existingIdAdmin = 1L;
        existingIdClient = 2L;
        nonExistingId = 1000L;
        countTotalElements = 5L;
        adminUsername = "Bob Brow";
        adminPassword = "123456";
        clientUsername = "Maria Green";
        clientPassword = "123456";
    }

    @Test
    public void findAllShouldReturnUnauthorizedWhenNoTokenGiven() throws Exception {

        ResultActions result = mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void findAllShouldReturnForbiddenWhenClientAuthenticated() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);

        ResultActions result = mockMvc.perform(get("/users")
                .header("Authorization", "Bearer" + accessToken)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isForbidden());
    }

    @Test
    public void findAllShouldReturnUsersPagedWhenAdminAuthenticated() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

        ResultActions result = mockMvc.perform(get("/users")
                .header("Authorization", "Bearer" + accessToken)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.totalElements").value(countTotalElements));
        result.andExpect(jsonPath("$.content").exists());
        result.andExpect(jsonPath("$.content[0].name").value("Bob Brow"));
        result.andExpect(jsonPath("$.content[1].name").value("Maria Green"));
        result.andExpect(jsonPath("$.content[2].name").value("Jordan Perish"));
    }

    @Test
    public void findByIdShouldReturnUnauthorizedWhenNoTokenGiven() throws Exception {

        ResultActions result = mockMvc.perform(get("/users/{id}", existingIdAdmin).contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void findByIdShouldReturnForbiddenWhenClientAuthenticated() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);

        ResultActions result = mockMvc.perform(get("/users/{id}", existingIdAdmin)
                .header("Authorization", "Bearer" + accessToken)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isForbidden());
    }

    @Test
    public void findByIdShouldReturnCurrentUserWhenClientAuthenticated() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);

        ResultActions result = mockMvc.perform(get("/users/{id}", existingIdClient)
                .header("Authorization", "Bearer" + accessToken)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingIdClient));
        result.andExpect(jsonPath("$.name").isNotEmpty());
        result.andExpect(jsonPath("$.birthDate").isNotEmpty());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenAdminAuthenticatedAndIdDoesNotExist() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

        ResultActions result = mockMvc.perform(get("/users/{id}", nonExistingId)
                .header("Authorization", "Bearer" + accessToken)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void findByIdShouldReturnClientUserWhenAdminAuthenticated() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

        ResultActions result = mockMvc.perform(get("/users/{id}", existingIdClient)
                .header("Authorization", "Bearer" + accessToken)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingIdClient));
        result.andExpect(jsonPath("$.name").isNotEmpty());
        result.andExpect(jsonPath("$.birthDate").isNotEmpty());
    }

    @Test
    public void findAddressOfPersonShouldReturnUnauthorizedWhenNoTokenGiven() throws Exception {

        ResultActions result = mockMvc.perform(get("/users/{id}/addresses", existingIdAdmin)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void findAddressOfPersonShouldReturnForbiddenWhenClientAuthenticated() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);

        ResultActions result = mockMvc.perform(get("/users/{id}/addresses", existingIdAdmin)
                .header("Authorization", "Bearer" + accessToken)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isForbidden());
    }

    @Test
    public void findAddressOfPersonShouldReturnCurrentAddressesUserWhenClientAuthenticated() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);

        ResultActions result = mockMvc.perform(get("/users/{id}/addresses", existingIdClient)
                .header("Authorization", "Bearer" + accessToken)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$[0].id").value(4L));
        result.andExpect(jsonPath("$[0].publicPlace").value("Rua Oscar Freire"));
        result.andExpect(jsonPath("$[1].id").value(5L));
        result.andExpect(jsonPath("$[1].publicPlace").value("Avenida Atlântica"));
    }

    @Test
    public void findAddressOfPersonShouldReturnAddressClientWhenAdminAuthenticated() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

        ResultActions result = mockMvc.perform(get("/users/{id}/addresses", existingIdClient)
                .header("Authorization", "Bearer" + accessToken)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$[0].id").value(4L));
        result.andExpect(jsonPath("$[0].publicPlace").value("Rua Oscar Freire"));
        result.andExpect(jsonPath("$[1].id").value(5L));
        result.andExpect(jsonPath("$[1].publicPlace").value("Avenida Atlântica"));
    }

    @Test
    public void insertPersonShouldReturnUnathorizedWhenNoTokenGiven() throws Exception {

        ResultActions result = mockMvc.perform(post("/users")
                .header("Authorization", "Bearer")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void insertPersonShouldReturnForbiddenWhenClientAuthenticated() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);

        UserInsertDTO userDTO = new UserInsertDTO();
        userDTO.setName("José Blue");
        userDTO.setBirthDate(Instant.parse("1999-08-01T23:00:00Z"));
        userDTO.setPassword("123456789");

        String jsonBody = objectMapper.writeValueAsString(userDTO);

        ResultActions result = mockMvc.perform(post("/users")
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isForbidden());
    }

    @Test
    public void insertPersonShouldReturnUnproccessableEntityWhenAdminAuthenticatedAndInvalidPassword() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

        UserInsertDTO userDTO = new UserInsertDTO();
        userDTO.setName("José Blue");
        userDTO.setBirthDate(Instant.parse("1999-08-01T23:00:00Z"));
        userDTO.setPassword("     ");

        String jsonBody = objectMapper.writeValueAsString(userDTO);

        ResultActions result = mockMvc.perform(post("/users")
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void insertPersonShouldReturnUnproccessableEntityWhenAdminAuthenticatedAndInvalidBirthDate() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

        UserInsertDTO userDTO = new UserInsertDTO();
        userDTO.setName("José Blue");
        userDTO.setBirthDate(Instant.parse("2024-08-01T23:00:00Z"));
        userDTO.setPassword("123456789");

        String jsonBody = objectMapper.writeValueAsString(userDTO);

        ResultActions result = mockMvc.perform(post("/users")
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void insertPersonShouldReturnUnproccessableEntityWhenAdminAuthenticatedAndInvalidName() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

        UserInsertDTO userDTO = new UserInsertDTO();
        userDTO.setName("     ");
        userDTO.setBirthDate(Instant.parse("1999-08-01T23:00:00Z"));
        userDTO.setPassword("123456789");

        String jsonBody = objectMapper.writeValueAsString(userDTO);

        ResultActions result = mockMvc.perform(post("/users")
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void insertPersonShouldInsertPersonWhenAdminAuthenticated() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

        UserInsertDTO userDTO = new UserInsertDTO();
        userDTO.setName("José Blue");
        userDTO.setBirthDate(Instant.parse("1999-08-01T23:00:00Z"));
        userDTO.setPassword("123456789");

        String jsonBody = objectMapper.writeValueAsString(userDTO);

        ResultActions result = mockMvc.perform(post("/users")
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());

        result.andExpect(jsonPath("$.id").isNotEmpty());
        result.andExpect(jsonPath("$.name").isNotEmpty());
        result.andExpect(jsonPath("$.birthDate").isNotEmpty());
    }

    @Test
    public void updatePersonShouldReturnUnathorizedWhenNoTokenGiven() throws Exception {

        ResultActions result = mockMvc.perform(put("/users/{id}", existingIdClient)
                .header("Authorization", "Bearer")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void updatePersonShouldReturnForbiddenWhenClientAuthenticated() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);

        UserUpdateDTO userDTO = new UserUpdateDTO();
        userDTO.setName("José Blue");
        userDTO.setBirthDate(Instant.parse("1999-08-01T23:00:00Z"));

        String jsonBody = objectMapper.writeValueAsString(userDTO);

        ResultActions result = mockMvc.perform(put("/users/{id}", existingIdAdmin)
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isForbidden());
    }

    @Test
    public void updatePersonShouldUpdateAddressCurrentUserWhenClientAuthenticated() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);

        UserUpdateDTO userDTO = new UserUpdateDTO();
        userDTO.setName("José Blue");
        userDTO.setBirthDate(Instant.parse("1999-08-01T23:00:00Z"));

        String jsonBody = objectMapper.writeValueAsString(userDTO);

        ResultActions result = mockMvc.perform(put("/users/{id}", existingIdClient)
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.id").value(existingIdClient));
        result.andExpect(jsonPath("$.name").value("José Blue"));
        result.andExpect(jsonPath("$.birthDate").value("1999-08-01T23:00:00Z"));
    }

    @Test
    public void updatePersonShouldReturnNotFoundWhenAdminAuthenticatedAndIdDoesNotExist() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

        UserUpdateDTO userDTO = new UserUpdateDTO();
        userDTO.setName("José Blue");
        userDTO.setBirthDate(Instant.parse("1999-08-01T23:00:00Z"));

        String jsonBody = objectMapper.writeValueAsString(userDTO);

        ResultActions result = mockMvc.perform(put("/users/{id}", nonExistingId)
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void updatePersonShouldReturnUnproccessableEntityWhenAdminAuthenticatedAndInvalidBirthDate() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

        UserUpdateDTO userDTO = new UserUpdateDTO();
        userDTO.setName("José Blue");
        userDTO.setBirthDate(Instant.parse("2024-08-01T23:00:00Z"));

        String jsonBody = objectMapper.writeValueAsString(userDTO);

        ResultActions result = mockMvc.perform(put("/users/{id}", existingIdClient)
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void updatePersonShouldReturnUnproccessableEntityWhenAdminAuthenticatedAndInvalidName() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

        UserUpdateDTO userDTO = new UserUpdateDTO();
        userDTO.setName("      ");
        userDTO.setBirthDate(Instant.parse("1999-08-01T23:00:00Z"));

        String jsonBody = objectMapper.writeValueAsString(userDTO);

        ResultActions result = mockMvc.perform(put("/users/{id}", existingIdClient)
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void updatePersonShouldUpdateUserWhenAdminAuthenticated() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

        UserUpdateDTO userDTO = new UserUpdateDTO();
        userDTO.setName("José Blue");
        userDTO.setBirthDate(Instant.parse("1999-08-01T23:00:00Z"));

        String jsonBody = objectMapper.writeValueAsString(userDTO);

        ResultActions result = mockMvc.perform(put("/users/{id}", existingIdClient)
                .header("Authorization", "Bearer" + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.id").value(existingIdClient));
        result.andExpect(jsonPath("$.name").value("José Blue"));
        result.andExpect(jsonPath("$.birthDate").value("1999-08-01T23:00:00Z"));
    }
}